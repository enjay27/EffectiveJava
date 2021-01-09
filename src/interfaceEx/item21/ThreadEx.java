package interfaceEx.item21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadEx implements Runnable {

    List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < 10; i++) {
                synchronizedList.add(i * 2);
            }
            System.out.println(Thread.currentThread().getName() + " add Numbers" + synchronizedList.toString());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(synchronizedList.removeIf(n -> (n % 2 == 0)))
                System.out.println(Thread.currentThread().getName() + " remove Numbers"+ synchronizedList.toString());

        }

    }
}
