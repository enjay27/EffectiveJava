# Generic with Effective Java 3rd

## Raw 타입

Raw Type 을 사용하는 경우 Generic Type 을 사용할 때와 같은 컴파일러의 검사를 받을 수 없다. 로 타입의 컬렉션은 컴파일러는 개발자의 의도와는 달리 모든 타입의 컬렉션을 허용하게 된다.

```java
public class DoNotUseRawType {

    private static final Collection stamps = new ArrayList();
    private static final Collection<Stamp> stampsWithGenerics = new ArrayList<>();

    public static Collection getStamps() {
        return stamps;
    }

    public static Collection<Stamp> getStampsWithGenerics() {
        return stampsWithGenerics;
    }

    static class Stamp {

    }

    static class Coin {

    }
}
```

```java
public class Item26 {
    public static void main(String[] args) {
        DoNotUseRawType.getStamps().add(new DoNotUseRawType.Coin()); //컴파일성공, 런타임에러
        //DoNotUseRawType.getStampsWithGenerics().add(new DoNotUseRawType.Coin()); //컴파일에러
    }
}
```

## 한정적 와일드카드 <?> 사용

메서드에도 마찬가지다. 로 타입의 파라미터인 경우 컬렉션에 아무 원소나 넣을 수 있어 불변식이 훼손된다.
메서드에서 타입 불변을 보장해야 하는 경우 비한정적 와일드카드 타입을 사용하면 된다.

```java
public void useRawTypeParameter(Set s1, Set s2) {
    s1.add(1000);
    s2.add("RAWTYPE");
}

public void useUnboundedWildTypeParameter(Set<?> s1, Set<?> s2) {
    //s1.add(1000); //컴파일에러
}
```

## 제네릭 적용이 불가능한 경우

제네릭을 쓰지 않는 경우도 있다. class literal 을 사용해야 하는 경우 제네릭 사용이 불가능하다.

```java
public void useClassLiteral() {
    Map<Class<?>, Object> map = new HashMap<>();
    map.put(String.class, "12323");
    map.put(Integer.class, 12323);
    map.put(List.class, new ArrayList<>());
    //map.put(List<Integer>.class, new ArrayList<>()); //컴파일 에러
}
```

instanceof 연산자의 경우에는 컴파일 완료 후 런타임 시점에 제네릭 타입 정보가 지워지기 때문에 불필요하다.

```java
public boolean useInstanceOf(Set<?> s1) {
    //return s1 instanceof Set<Integer>; //컴파일 에러
    return s1 instanceof Set<?> && s1 instanceof Set; // 와일드카드 타입은 쓸 수 있다
}
```

## 제네릭으로 유연한 메서드 만들기

파라미터에 제네릭을 쓴 경우 매개변수 타입이 완전히 일치해야만 동작한다.

```java
public void pushAll(Iterable<E> src) {
    src.forEach(this::push);
}
```

```java
public static void main(String[] args) {
    UseUnboundedWildCard<Number> numberStack = new UseUnboundedWildCard<>();
    Iterable<Integer> integers = new ArrayList<>();
    //numberStack.pushAll(integers); //컴파일에러
}
```

이 경우 한정적 와일드카드를 사용하여 매개변수의 범위를 늘릴 수 있다. E의 하위타입 전부를 받는 Iterator 매개변수로 범위를 넓혔다.

```java
public void pushAll(Iterable<? extends E> src) {
    src.forEach(this::push);
}
```

```java
public static void main(String[] args) {
    UseUnboundedWildCard<Number> numberStack = new UseUnboundedWildCard<>();
    Iterable<Integer> integers = new ArrayList<>();
    numberStack.pushAll(integers);
}
```

pop도 마찬가지다. 매개변수 타입이 일치하지 않아 컴파일에러가 발생한다.

```java
public void popAll(Collection<E> dst) {
    while(isEmpty())
        dst.add(pop());
}
```

```java
public static void main(String[] args) {
    UseUnboundedWildCard<Number> numberStack = new UseUnboundedWildCard<>();
    Iterable<Integer> integers = new ArrayList<>();
    numberStack.pushAll(integers);

    Collection<Object> objects = new ArrayList<>();
    //numberStack.popAll(objects); //컴파일에러
}
```

pop 메서드는 E의 상위타입 전부여야 한다. 바꾸고 나면 컴파일에러가 없어진다

```java
public void popAll(Collection<? super E> dst) {
    while(isEmpty())
        dst.add(pop());
}
```

```java
public static void main(String[] args) {
    UseUnboundedWildCard<Number> numberStack = new UseUnboundedWildCard<>();
    Iterable<Integer> integers = new ArrayList<>();
    numberStack.pushAll(integers);

    Collection<Object> objects = new ArrayList<>();
    numberStack.popAll(objects);
}
```

PECS : producer - extends, consumer - super
생산자라면 extends, 소비자라면 super 를 사용한다. Comparable과 Comparator는 언제나 소비자이므로 Comparable<? super E>와 Comparator<? super E> 를 사용하는 게 낫다.

### Helper Method

```java
public static <E> void swapGeneric(List<E> list, int i, int j){}
public static void swapWildCard(List<?> list, int i, int j){}
```

public API 라면 와일드카드를 사용하는 것이 낫다.

```java
public static void swapWildCard(List<?> list, int i, int j){
    list.set(i, list.set(j, list.get(i))); // 컴파일 에러
}
```

와일드카드 타입의 컬렉션에는 null 이외의 값을 넣을 수 없기 때문에, 값을 넣어야 하는 경우는 Helper Method 를 사용한다.

```java
public static void swapWildCard(List<?> list, int i, int j){
    swapHelper(list, i, j);
}
private static <E> void swapHelper(List<E> list, int i, int j){
    list.set(i, list.set(j, list.get(i)));
}
```
