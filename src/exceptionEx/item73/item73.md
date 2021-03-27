## 아이템 73. 추상화 수준에 맞는 예외를 던지라

### 1. Exception Translation (예외 번역)

저수준 예외가 발생했을 때 처리가 불가능하다면 수준에 맞는 에러로 바꿔서 던져야 한다.
```java
try {
    (...) // LowerLevelException 발생 코드
} catch (LowerLevelException e) {
    throw new HigherLeverlException(...); // 수준에 맞게 변환
}
```
처리 없이 에러가 던져지는 경우 내부 구현 방식을 드러내어 상위 레벨 API를 오염시킨다.   

아래는 더 구체적인 예시다.
```java
public E get(int index) {
    ListIterator<E> i = listIterator(index);
    try{
        return i.next();
    } catch (NoSuchElementException e) {
        throw new IndexOutOfBoundException("index : " + index);
    }
}
```
---

### 2. Exception Chaining (예외 연쇄)

예외 연쇄란 문제의 원인인 저수준 예외를 고수준 예외에 실어 보내는 방식이다. 
```java
try {
    (...)
} catch (LowerLevelException cause) {
    throw new HigherLevelException(cause); // 고수준에 실어 보낸다.    
}
```
이렇게 하면 접근자 메서드(Throwable getCause 메서드) 를 통해 필요하면 언제든 저수준 예외를 꺼내 볼 수 있다.

```java
class HigherLevelException extends Exception {
    HigherLeverlException(Throwable cause) {
        super(cause);
    }
}
```
고수준 예외의 생성자는 상위 클래스의 생성자에 원인(cause) 를 건네주어 Throwable 생성자까지 건네지게 한다.

---

### 3. 남용은 금지

예외 전파보단 나은 방법이지만 남용하는 것보단 저수준 메서드가 반드시 성공하도록 하여 아래 계층에서는 예외가 발생하지 않도록 하는 것이 최선이다.   

이 외엔 상위계층에서 예외를 처리하는 방법도 있는데, 이 경우 java.util.logging 같은 로깅 기능을 활용하여 기록해두고 로그를 분석해 추가 조치를 취할 수 있게 하는 것이 좋다.