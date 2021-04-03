## 아이템 78. 공유 중인 가변 데이터는 동기화해 사용하라

### Synchronized
 해당 메서드나 블록을 한번에 한 스레드씩 수행하도록 보장한다.   
 락을 건 메서드는 객체의 상태를 확인하고 필요하면 수정한다.
 
 추가로 동기화는 다른 스레드가 락의 보호 하에 수정된 결과를 보게 해준다.
 
 - 동기화는 배타적 실행뿐 아니라 스레드 사이의 안정적인 통신에 꼭 필요하다.

### __Thread.stop 메서드는 안전하지 않아 deprecated API 로 지정되었다.__
 ```java
public class StopThread {
    public static boolean stopRequested;
    
    public static void main(String[] args) throws InterruptedException {
       Thread backgroundThread = new Thread(() -> {
           int i = 0; 
           while (!stopRequested)
               i++;
       });
       backgroundThread.start();
    
       TimeUnit.SECONDS.sleep(1);
       stopRequested = true;
    }
}
 ```
무한루프를 빠져나가는 코드가 없어보인다.    
실제로 동작해보면 응답 불가(liveness failure) 상태가 되어 진전이 없다.

```java
public class StopThread {
    public static boolean stopRequested;
    
    private static synchronized void requestStop() {
        stopRequested = true;
    }
    
    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
```
필드를 동기화하는 부분을 추가하면 기대한 대로 동작하게 된다.   
__쓰기(requestStop)와 읽기(stopRequested) 모두를 동기화했는데, 둘 모두가 동기회되지 않으면
동작을 보장하지 않는다.__
 

반복분에서는 필드를 volatile로 선언하여 동기화가 가능하다.    
이 한정자는 배타적 수행과는 상관없이 가장 최근에 기록된 값을 읽게 됨을 보장한다.
```java
public class StopThread {
    public static volatile boolean stopRequested;

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
```
### volatile은 주의해서 사용해야 한다.
```java
private static volatile int nextSerialNumber = 0;

public static int generateSerialNumber() {
    return nextSerialNumber++;
}
```
이 메서드는 증가(++) 연산자 때문에 스레드 동기화가 필요하다.   
접근하고 증가시키는 동작 사이에 스레드 안전이 필요하다.   
프로그램이 잘못된 결과를 계산해내는 이런 오류를 안전 실패(safety failure)라고 한다.

__메서드에 synchronized를 붙였다면 nextSerialNumber 필드에서는 volatile을 제거해야 한다.__

### Atomic 패키지

이 패키지에는 스레드 안전한 프로그래밍을 지원하는 클래스들이 담겨 있다.   
```java
private static final AtomicLong nextSerialNum = new AtomicLong();

public static long generateSerialNumber() {
    return nextSerialNum.getAndIncrement();
}
```
Atomic은 동기화의 두 효과인 통신과 원자성까지 모두 지원한다.

### 가변데이터 공유?

이번 아이템에서의 문제는 가변 데이터를 공유한 경우에 생겼다.   
__불변 데이터만 공유하거나 아무것도 공유하지 말자.__   
가변 데이터는 단일 스레드에서만 써야 한다.

