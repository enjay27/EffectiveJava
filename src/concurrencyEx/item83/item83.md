## 아이템 83. 지연 초기화는 신중히 사용하라

> __지연 초기화 (lazy initialization)__
>  - 필드의 초기화 시점을 그 값이 처음 필요할 때까지 늦추는 것

멀티 스레드 환경에서는 지연 초기화를 하기가 까다롭다. 지연 초기화를 하는 필드를 공유한다면 어떤 형태로든 동기화해야 한다.

따라서 대부분 상황에서 일반 초기화가 지연 초기화보다 낫다.

### 지연 초기화 홀더 클래스 (lazy initialization holder class)
성능때문에 정적 필드를 지연 초기화해야 한다면 지연 초기화 홀더 클래스(lazy initialization holder class) 관용구를 사용하자.
```java
 // Lazy initialization holder class idiom for static fields - Page 334
private static class FieldHolder {
    static final FieldType field = computeFieldValue();
}

private static FieldType getField() { return FieldHolder.field; }
```
getField 메서드가 처음 호출된 순간 FileHolder.filed 가 처음 읽히면서 FieldHolder 클래스를 초기화한다.

### 이중검사 (double-check)
성능때문에 인스턴스 필드를 지연 초기화해야 한다면 이중 검사 관용구를 사용하라.    
값을 두 번 검사하는 방식으로, 한 번은 동기화 없이 검사하고, 두 번째는 동기화하여 검사한다.   
두 번째 검사에서도 필드가 초기화되지 않았을 때만 필드를 초기화한다.   
필드가 초기화 된 후 동기화하지 않으므로 반드시 volatile로 선언해야 한다.
```java
// Double-check idiom for lazy initialization of instance fields - Page 334
private volatile FieldType field4;

// NOTE: The code for this method in the first printing had a serious error (see errata for details)!
private FieldType getField4() {
    FieldType result = field4;
    if (result != null)    // First check (no locking)
        return result;

    synchronized(this) {
        if (field4 == null) // Second check (with locking)
            field4 = computeFieldValue();
        return field4;
    }
}
```
result 라는 지역변수를 사용하여 필드가 이미 초기화된 상황에서 필드를 딱 한 번만 읽도록 보장한다.

### 단일 검사 (single-check)
반복해서 초기화해도 상관없는 인스턴스 필드를 지연 초기화해야 하는 경우, 이중검사에서 두 번째 검사를 생략할 수 있다.
```java
// Single-check idiom - can cause repeated initialization! - Page 334
private volatile FieldType field5;

private FieldType getField5() {
    FieldType result = field5;
    if (result == null)
        field5 = result = computeFieldValue();
    return result;
}

private static FieldType computeFieldValue() {
    return new FieldType();
}
```
필드는 여전히 volatile로 선언해야 한다.

### volatile
모든 스레드가 필드의 값을 다시 계산해도 사오간없고 필드의 타입이 long, double을 제외한 다른 기본 타입이라면,
단일검사의 필드 선언에서 volatile 한정자를 없애도 된다. (거의 쓰지 않음)