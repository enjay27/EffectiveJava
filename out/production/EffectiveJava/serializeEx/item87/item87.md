## 아이템 87. 커스텀 직렬화 형태를 고려해보라

### 기본 직렬화가 적합한 클래스
객체의 물리적 표현과 논리적 내용이 같은 형태

기본 직렬화 형태가 적합하더라도 readObject 메서드를 제공해야 할 때가 많다.

### 적합하지 않은 클래스

```java
import java.io.Serializable;

// StringList with a reasonable custom serialized form  - Page 349
public final class StringList implements Serializable {
    private int size = 0;
    private Entry head = null;

    private static class Entry implements Serializable {
        String data;
        Entry next;
        Entry previous;
    }
    ...
}
```
doubly linked list 로 연결된 이 클래스에 기본 직렬화 형태를 사용하면 각 노드의 양방향 연결 정보를 포함해 모든 엔트리를 기록한다.

다음과 같은 문제가 생긴다.
1. 공개 API가 현재의 내부 표현 방식에 영구히 묶인다. 다음 릴리즈에서 바꾸더라도 연결 리스트를 지원해야한다.
2. 모든 엔트리를 기록하여 많은 공간을 차지하게 된다.
3. 시간이 많이 걸린다. 그래프를 직접 순회해야 한다.
4. 스택 오버플로. 기본 직렬화 과정은 객체 그래프를 재귀 순회하여 스택 오버플로를 일으킨다.


### 위 클래스의 합리적인 직렬화 형태
```java
public final class StringList implements Serializable {
    private transient int size = 0;
    private transient Entry head = null;

    // No longer Serializable!
    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    // Appends the specified string to the list
    public final void add(String s) {
    }

    /**
     * Serialize this {@code StringList} instance.
     *
     * @serialData The size of the list (the number of strings
     * it contains) is emitted ({@code int}), followed by all of
     * its elements (each a {@code String}), in the proper
     * sequence.
     */
    private void writeObject(ObjectOutputStream s)
            throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (Entry e = head; e != null; e = e.next)
            s.writeObject(e.data);
    }

    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();

        // Read in all elements and insert them in list
        for (int i = 0; i < numElements; i++)
            add((String) s.readObject());
    }

    // Remainder omitted
}
```
리스트가 포함한 문자열의 개수를 적은 다음, 문자열들을 나열한다.   
transient 한정자는 해당 인스턴스 필드가 기본 직렬화 형태에 포함되지 않는다는 표시다.

writeObject 와 readObject 둘 다 default 메서드를 호출한다. 직렬화 명세에 따라 transient 를 사용해도 
호출해야 한다. 
이래야 향후 릴리즈에서 transient 를 사용하지 않는 필드가 추가되더라도 상호 호환된다.

writeObject 는 private 임에도 문서화 주석이 달려 있다. 이 메서드는 직렬화 형태에 포함되는 공개 API 에 속한다.


defaultWriteObject 메서드를 호출하면 transient 로 선언하지 않은 모든 인스턴스 필드가 직렬화된다.
따라서 transient 를 붙일 수 있는 모든 인스턴스 필드에는 모두 붙여야 한다.

기본 직렬화를 사용한다면 transient 필드들은 역직렬화될 때 기본값으로 초기화된다. 지연초기화가 필요하다면 지연초기화를 사용하자.

스레드 안전하게 만든 객체에 기본 직렬화를 적용하려면 writeObject 메서드도 synchronized 로 선언해야 한다.
```java
private synchronized void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
}
```

직렬화 가능 클래스 모두에 직렬 버전 UID 를 명시적으로 부여하자. UID 가 일으키는 호환성 문제를 방지한다.
```java
private static final long serialVersionUID = <LONG VALUE>;
```

구버전으로 직렬화된 인스턴스와 호환성을 유지하고 싶다면, 자동 생성된 값을 그대로 사용해야 한다. 

호환성을 끊고 싶다면 UID 값을 바꾸면 된다. 끊으려는 경우를 제외하고는 UID 는 바뀌어서는 안 된다.