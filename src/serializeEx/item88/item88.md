## 아이템 88. readObject 메서드는 방어적으로 작성하라



### 불변 객체 직렬화의 문제점

```java
public final class Period {
    private final Date start;
    private final Date end;

    /**
     * @param  start the beginning of the period
     * @param  end the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException if start or end is null
     */
    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0)
            throw new IllegalArgumentException(
                    start + " after " + end);
        this.start = start;
        this.end = end;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }

    public String toString() {
        return start + " - " + end;
    }
}
```

item 50에서 불변을 유지하기 위해 생성자와 접근자에서 객체를 방어적으로 복사했다.   
이 클래스를 직렬화 하는 경우 implements Serializable 을 붙이는 것으로는 불변식을 보장할 수 없다.   
readObject 메서드가 또 다른 생성자이기 때문이다.
- readObject : 매개변수로 바이트 스트림을 받는 생성자


### 해결 방법 1

__Period 의 readObject 메서드에서 defaultReadObject 호출 후 역직렬화된 객체가 유효한지 검사해야 한다.__   
실패한 경우 InvalidObjectException 을 던지게 하여 잘못된 역직렬화를 막는다.

```java
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    
    if(start.compareTo(end) > 0)
        throw new InvalidObjectException(start + " later than " + end);
}
```
- 문제점 : 정상 Period 인스턴스에서 시작된 바이트 스트림 끝에 
  private __Date__ 필드로의 참조를 추가하면 가변 Period 인스턴스를 만들 수 있다.   
  이후 Period 객체의 내부 정보를 얻어 수정이 가능해진다. (불변식이 깨짐)
  
### 가변 공격
```java
public class MutablePeriod {
  // Period instance
  public final Period period;

  // start time field
  public final Date start;

  // end time field
  public final Date end;

  public MutablePeriod() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);

      // Serialize valid Period instance
      out.writeObject(new Period(new Date(), new Date()));

      /*
       * previous object reference
       */
      byte[] ref = { 0x71, 0, 0x7e, 0, 5 };
      bos.write(ref); // start field
      ref[4] = 4;
      bos.write(ref); // end field

      // steal Date reference after serialize Period
      ObjectInputStream in = new ObjectInputStream(
              new ByteArrayInputStream(bos.toByteArray()));
      period = (Period) in.readObject();
      start = (Date) in.readObject();
      end = (Date) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new AssertionError(e);
    }
  }
}
```
Period 의 readObject 메서드가 방어적 복사를 충분히 하지 않아서 공격이 가능하다.    
__객체를 역직렬화할 때는 클라이언트가 소유해서는 안 되는 객체 참조를 갖는 필드를 모두 방어적 복사해야 한다.__

### 방어적 복사
```java
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        
        // copy editable elements
        start = new Date(starg.getTime());
        end = new Date(end.getTime());
        
        // check immutable
        if (start.compareTo(end) > 0)
            throw new InvalidObjectException(start + " is later than " + end);
}
```
start, end 필드를 방어적 복사 한다. 여기서 clone 메서드는 사용하지 않았다.    
여기서 final 필드는 방어적 복사가 불가능하기 때문에 readObject 메서드를 사용하려면 final 한정자를 제거해야 한다.

- transient 필드를 제외한 모든 필드의 값은 모두 유효성 검사와 방어적 복사를 수행해야 한다.   
또는 item90의 직렬화 프록시 패턴을 사용한다.
  

- final 이 아닌 직렬화 가능 클래스라면 readObject 메서드에서 재정의 가능 메서드를 호출해서는 안 된다.    
호출한 메서드가 재정의되면 하위 클래스의 상태가 완전히 역직렬화되기 전에 하위 클래스에서 재정의된 메서드가 실행되어 프로그램 오작동으로 이어진다. 
