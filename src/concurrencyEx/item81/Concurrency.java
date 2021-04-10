package concurrencyEx.item81;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Concurrency {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();

        System.out.println(ConcurrentTimer.time(executor, 5, () -> {})); // 134900
        System.out.println(ConcurrentTimer.time(executor, 10, () -> {})); // 75900
        System.out.println(ConcurrentTimer.time(executor, 15, () -> {})); //97200
        System.out.println(ConcurrentTimer.time(executor, 20, () -> {})); // 154700
    }

    static class ThreadTest extends Thread {
        public void run() {
            Executor executor = Executors.newSingleThreadExecutor();
            try {
                ConcurrentTimer.time(executor, 1, () -> System.out.println("end"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
