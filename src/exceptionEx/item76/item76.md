## 아이템 76. 가능한 한 실패 원자적으로 만들라

___
호출된 메서드가 실패하더라도 해당 객체는 메서드 호출 전 상태를 유지해야 한다.

이 특성을 실패 원자적(failure-atomic) 이라고 한다. 메서드를 실패 원자적으로 만드는 방법은 여러가지가 있다. 

___

### 1. 불변 객체

불변 객체는 태생적으로 실패 원자적이다. 불변 객체의 상태는 생성 시점에 고정되어 절대 변하지 않기 때문에 불안정한 상태에 빠지는 일은 결코 없다.

___

### 2. 가변 객체
```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null;
    return result;
}
```
가변 객체의 경우 작업 수행 전 매개변수의 유효성을 검사해야 한다.
객체의 상태를 변경하기 전 잠재적 예외 가능성을 걸러낼 수 있다.

###

```java
public void putAll(Map<? extends K, ? extends V> map) {
    int mapSize = map.size();
    if (size==0 && mapSize!=0 && map instanceof SortedMap) {
        Comparator<?> c = ((SortedMap<?,?>)map).comparator();
        if (c == comparator || (c != null && c.equals(comparator))) {
            ++modCount;
            try {
                buildFromSorted(mapSize, map.entrySet().iterator(),
                                null, null);
            } catch (java.io.IOException | ClassNotFoundException cannotHappen) {
            }
            return;
        }
    }
    super.putAll(map);
}
```
비슷한 취지로 실패할 가능성이 있는 모든 코드를 객체의 상태를 바꾸는 코드보다 앞에 배치하는 방법도 있다.
위의 코드에서 인수로 들어오는 map 에 대하여 저장된 타입에 유효한 지 검사를 한 후 putAll 을 실행한다.
   
만약 기대한 것과 다른 타입이 들어온 경우 ClassCastException 을 던질 것이다.

---
### 3. 임시 복사본
이 방법은 객체의 임시 복사본에서 작업을 수행한 다음, 작업이 성공적으로 완료되면 원래 객체와 교체하는 것이다.

이렇게 하면 작업이 실패하더라도 원래 객체를 반환하면 되기 때문에 실패 원자성이 보장된다.


---

### 4. 복구 코드
작업 도중 발생하는 실패를 가로채 작업 전 상태로 되돌린다. 

내구성(durability) 을 보장해야 하는 자료구조에 쓰인다.

___


실패 원자성을 항상 보장하는 것은 불가능하다. 

다만 실패 원자성을 보장하지 않는 메서드의 경우는 문제를 인지하고 있어야 한다.