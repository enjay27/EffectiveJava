## 아이템 72. 표준 예외를 사용하라

---

###1.코드는 재사용을 해야 한다.

코드는 재사용이 가능하다. 예외도 마찬가지며 표준 예외를 사용 한 경우 재사용이 용이해진다.

자주 쓰이는 예외는 아래와 같다.

```java
throw new IllegalArgumentException(); // 허용되지 않는 값이 인수로 건네졌을 때
throw new IllegalStateException(); // 객체가 메서드를 수행하기에 적절하지 않은 상태일 때
throw new NullpointerException(); // null 처리가 불가능할 때
throw new IndexOutOfBoundsException(); // 인덱스가 허용 범위를 넘어섰을 때
throw new ConcurrentModificationException(); // 허용하지 않는 동시 수정이 발견됐을 때
throw new UnsupportedOperationException(); // 호출한 메서드를 지원하지 않을 때
```

이 때 Exception, RuntimeException, Throwable, Error 는 재사용하면 안 된다. 위의 예외들을 포괄하는 클래스이므로 명확한 뜻을 건네주는 것이 불가능하다.

----

이처럼 예외는 해당 상황에 대한 정보를 제공해야 한다.
