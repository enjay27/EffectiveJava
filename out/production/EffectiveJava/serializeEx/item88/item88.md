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
    // Period 인스턴스
    public final Period period;
    
}
```