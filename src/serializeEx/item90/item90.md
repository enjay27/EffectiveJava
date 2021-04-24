## 아이템 90. 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라

### 직렬화 프록시

```java
// Serialization proxy for Period class
private static class SerializationProxy implements Serializable {
    private final Date start;
    private final Date end;

    SerializationProxy(Period p) {
        this.start = p.start;
        this.end = p.end;
    }

    private static final long serialVersionUID =
            234098243823485285L; // Any number will do (Item 87)
}
```

바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계하여 private static 으로 선언한다.
이 중첩 클래스가 바깥 클래스의 직렬화 프록시다.

생성자는 단 하나여야 하며, 바깥 클래스를 매개변수로 받아야 한다.    
생성자는 넘어온 인스턴스의 데이터를 복사한다. 방어적 복사나 검사도 필요 없다.

바깥 클래스와 직렬화 프록시 모두 Serializable 을 구현해야 한다.

```java
// writeReplace method for the serialization proxy pattern
private Object writeReplace() {
    return new SerializationProxy(this);
}
```
다음으로 바깥 클래스에 writeReplace 메서드를 추가한다. 이 메서드는 자바의 직렬화 시스템이 
바깥 클래스의 인스턴스 대신 SerializationProxy의 인스턴스를 반환하게 한다. 직렬화 전에 
바깥 클래스의 인스턴스를 직렬화 프록시로 변환한다.

```java
// readObject method for the serialization proxy pattern
private void readObject(ObjectInputStream stream)
        throws InvalidObjectException {
    throw new InvalidObjectException("Proxy required");
}
```
readObject 메서드는 항상 실패하게 만들어 바깥 클래스의 직렬화를 막는다. 

```java
// readResolve method for PeriodSerializationProxy
private Object readResolve() { 
    return new Period(start, end);    
}
```
readResolve 메서드는 공개된 API만을 이용해 바깥 클래스의 인스턴스를 생성한다.    
일반 인스턴스를 만들 때와 똑같은 생성자, 적적 팩터리, 혹은 다른 메서드를 사용해 역직렬화된 인스턴스를 생성한다.


### 장점
가짜 바이트 스트림 공격과 내부 필드 탈취 공격을 프록시 수준에서 차단한다.

이전의 방법과는 달리 Period의 필드를 final 로 선언할 수 있어 불변으로 만들 수 있다.

역직렬화한 인스턴스와 원래의 직렬화된 인스턴스의 클래스가 달라도 정상 작동한다.

A의 하위 클래스 AA, AB가 있을 때,   
처음 직렬화 했을 때에는 AA 였다가 직렬화 한 후 원소를 추가 혹은 감소시킨 후 역직렬화 하면 AB가 된다.
 
### 단점
클라이언트가 확장이 불가능하다.

객체 그래프에 순환이 있는 클래스에는 적용할 수 없다.    
readResolve 안에서 객체의 메서드를 호출하려고 하면 ClassSCastException 이 발생한다.

방어적 복사보다 속도가 느리다.