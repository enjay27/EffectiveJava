## 아이템 81. wait와 notify보다는 동시성 유틸리티를 애용하라

### 동시성 유틸리티
> wait와 notify는 올바르게 사용하기가 까다롭다.
대신 사용할 고수준 유틸리티는 세 범주로 나뉜다.    
> - 실행자 프레임워크, 동시성 컬렉션, 동기화 장치

### 실행자 프레임워크
> item80 으로

### 동시성 컬렉션 (concurrent collection)
List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 구현한 고성능 컬렉션이다.   
동기화를 각자의 내부에서 수행하기 때문에 동시성을 무력화하는 건 불가능하며, 
외부에서 락을 추가로 사용하면 오히려 느려진다. (item79)

```java
private static final ConcurrentMap<String, String> map =
            new ConcurrentHashMap<>();

public static String intern(String s) {
    String previousValue = map.putIfAbsent(s, s);
    return previousValue == null ? s : previousValue;
}
```
putIfAbsent 메서드는 주어진 키에 값이 없을 경우에만 새 값을 넣는다. 
덕분에 스레드 안전한 정규화 맵을 쉽게 구현할 수 있다.

여기서 ConcurrentHashMap은 get같은 검색 기능에 최적화되었기 때문에
get을 먼저 호출하고 putIfAbsent를 사용하면 더 빨라진다.

```java
public static String intern(String s) {
    String result = map.get(s);
    if (result == null) {
        result = map.putIfAbsent(s, s);
        if (result == null)
            result = s;
    }
    return result;
}
```

Collections.synchronizedMap 보다는 ConcurrentHashMap을 사용하는 게 훨씬 좋다. 
동시성 맵으로 교체할 경우 성능이 개선된다.

###


### 동기화 장치
컬렉션 인터페이스 중 일부는 작업이 성공적으로 완료될 때까지 기다리도록(차단) 확장되었다.
BlockingQueue take 메서드는 큐가 비었다면 새로운 원소가 추가될 때까지 기다린다.    
생산자-소비자 형태로 생산자 스레드가 작업을 추가하고, 소비자 스레드가 큐의 작업을 처리하는 형태로 사용하기 적합하다.

> 동기화 장치 : 스레드가 다른 스레드를 기다릴 수 있게 하여 서로 작업을 조율할 수 있게 해준다.   
    CountDownLatch, Semaphore 가 자주 쓰이고 CyclicBarrier, Exchanger 가 그다음,    
    가장 강력한 장치는 Phaser 다.

###

동기화 장치를 사용하면 구현을 쉽게 할 수 있다.
```java
public static long time(Executor executor, int concurrency,
                            Runnable action) throws InterruptedException {
    CountDownLatch ready = new CountDownLatch(concurrency);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done  = new CountDownLatch(concurrency);

    for (int i = 0; i < concurrency; i++) {
        executor.execute(() -> {
            ready.countDown(); // Tell timer we're ready
            try {
                start.await(); // Wait till peers are ready
                action.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();  // Tell timer we're done
            }
        });
    }

    ready.await();     // Wait for all workers to be ready
    long startNanos = System.nanoTime();
    start.countDown(); // And they're off!
    done.await();      // Wait for all workers to finish
    return System.nanoTime() - startNanos;
}
```
위에서는 카운트다운 래치를 3개 사용한다.    
ready 래치가 작업자 스레드들이 준비가 완료됐음을 알린다. 
이후 작업자 스레드가 모두 준비될 때까지 기다렸다가 모든 작업자 스레드가 준비중이면 action을 실행시킨다.
ready를 대기상태로 두고 start.countDown 을 호출하여 작업자 스레드를 깨운다.
마지막 작업자가 done.countDown 을 호출하여 done 이 열리면 종료 시각을 기록하고 끝낸다.

```java
public static void main(String[] args) throws InterruptedException {
    Executor executor = Executors.newCachedThreadPool();

    System.out.println(ConcurrentTimer.time(executor, 5, () -> {})); // 134900
    System.out.println(ConcurrentTimer.time(executor, 10, () -> {})); // 75900
    System.out.println(ConcurrentTimer.time(executor, 15, () -> {})); //97200
    System.out.println(ConcurrentTimer.time(executor, 20, () -> {})); // 154700
}
```

### wait & notify
어쩔 수 없이 wait & notify 를 써야하는 경우 wait은 반복문 안에서 호출해야 한다.  
조건이 충족된 경우 wait 를 건너뛰게 하여 응답 불가 상태를 예방한다.   
조건이 충족되지 않은 경우 wait 를 하게 하는 것은 안전 실패를 막는 조치다. 

### notify & notifyAll
둘 중에는 notifyAll 을 사용하는 게 더 안전하다.    
깨어나야 하는 모든 스레드가 깨어남을 보장한다.    
조건이 맞지 않아 대기중이던 스레드는 조건문을 거쳐 다시 대기상태가 된다.

