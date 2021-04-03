## 아이템 79. 과도한 동기화는 피하라

### 제어를 넘기면 안 된다
응답 불가와 안전 실패를 피하려면 동기화 메서드나 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안 된다.
```java
// Broken - invokes alien method from synchronized block!
public class ObservableSet<E> extends ForwardingSet<E> {
    public ObservableSet(Set<E> set) {
        super(set);
    }

    private final List<SetObserver<E>> observers
            = new ArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        synchronized(observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized(observers) {
            return observers.remove(observer);
        }
    }

    private void notifyElementAdded(E element) {
        synchronized(observers) {
            for (SetObserver<E> observer : observers)
                observer.added(this, element);
        }
    }

    // Alien method moved outside of synchronized block - open calls
    private void notifyElementAdded(E element) {
        List<SetObserver<E>> snapshot = null;
        synchronized(observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (SetObserver<E> observer : snapshot)
            observer.added(this, element);
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added)
            notifyElementAdded(element);
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c)
            result |= add(element);  // Calls notifyElementAdded
        return result;
    }
}
```
```java
// Set obeserver callback interface - Page 266
public interface SetObserver<E> {
    // Invoked when an element is added to the observable set
    void added(ObservableSet<E> set, E element);
}
```
```java
// More complex test of ObservableSet - Page 318-9
public class Test2 {
    public static void main(String[] args) {
        ObservableSet<Integer> set =
                new ObservableSet<>(new HashSet<>());

        set.addObserver(new SetObserver<>() { // this를 넘겨야 해서 람다 대신 익명클래스 사용
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23)
                    s.removeObserver(this);
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
```
기대하는 동작은 23까지 출력한 후 removeObserver를 호출하고 종료되는 것이다.   
하지만 23까지 출력 후 CurruntModificationnException을 던진다.   
added 메서드가 리스트 순회 도중에 리스트의 원소를 제거하려 했기 때문이다.   

notifyElementAdded 메서드는 동기화 블록 안에 있으므로 스레드 안전은 보장했지만,    
__자신이 콜백을 거쳐 되돌아와 수정하는 것까지 막지는 못한다.__

### 교착 상태
```java
// Simple test of ObservableSet - Page 319
public class Test3 {
    public static void main(String[] args) {
        ObservableSet<Integer> set =
                new ObservableSet<>(new HashSet<>());

// Observer that uses a background thread needlessly
        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec =
                            Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> s.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
```
이 프로그램은 교착상태에 빠진다. 백그라운드 스레드가 s.removeObserver를 호출하고 락을 얻어야 하는데   
메인 스레드가 이미 락을 쥔 상태로 백그라운드 스레드가 23을 제거하기만을 기다리고 있다.

자바 락은 재진입(reentrant)을 허용하므로 교착상태에 빠지지는 않는다.    
이것은 멀티스레드 프로그램을 쉽게 구현할 수 있도록 해주지만,    
__응답 불가(교착상태)가 될 상황을 안전 실패(데이터 훼손)로 변모시킬 수도 있다.__

```java
// Alien method moved outside of synchronized block - open calls
private void notifyElementAdded(E element) {
    List<SetObserver<E>> snapshot = null;
    synchronized(observers) {
        snapshot = new ArrayList<>(observers);
    }
    for (SetObserver<E> observer : snapshot)
        observer.added(this, element);
}
```
이 문제는 외계인 메서드 호출을 동기화 블록 바깥으로 옮기면 해결된다.    
observers 리스트를 복사해 순회하기 때문에 락 없이도 안전하다.

### CopyOnWriteArrayList
위의 메서드처럼 복사 후 순회만 한다면 CopyOnWriteArrayList 를 쓰면 된다.
```java
// Thread-safe observable set with CopyOnWriteArrayList
private final List<SetObserver<E>> observers =
        new CopyOnWriteArrayList<>();

public void addObserver(SetObserver<E> observer) {
    observers.add(observer);
}

public boolean removeObserver(SetObserver<E> observer) {
    return observers.remove(observer);
}

private void notifyElementAdded(E element) {
    for (SetObserver<E> observer : observers)
        observer.added(this, element);
}
```
명시적으로 동기화한 곳이 사라졌다.

### 열린 호출(open call)
동기화 영역 바깥에서 호출되는 외계인 메서드를 열린 호출이라고 한다.   
__동기화의 기본 규칙은 동기화 영역에서는 가능한 한 일을 적게 하는 것이다.__   

가변 클래스는 두 가지 중 하나가 되어야 한다.   
- 동기화를 전혀 하지 말고, 그 클래스를 동시에 사용해야 하는 클래스가 외부에서 동기화하게 하는 것.
  - java.util (Vector, Hashtable 제외)
- 동기화를 내부에서 수행해 스레드 안전한 클래스로 만드는 것.
  - java.util.concurrent
    
StringBuffer 이후 뒤늦게 StringBuilder 가 등장한 이유.

### 정리
__여러 스레드가 호출할 가능성이 있는 메서드가 정적 필드를 수정한다면 필드 사용 전 반드시 동기화해야 한다.__   
여러 스레드에서 구동되는 상황이라면 다른 클라이언트에서 메서드를 호출하는 걸 막을 수 없으니 외부에서 동기화할 방법이 없다.  
꼭 내부에서 동기화 하자. __+문서화__


