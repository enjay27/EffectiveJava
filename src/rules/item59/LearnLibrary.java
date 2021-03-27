package rules.item59;

import java.util.Comparator;
import java.util.Random;

public class LearnLibrary {
    static Random rnd = new Random();

    static int random(int n) {
        return Math.abs(rnd.nextInt(n)) % n;
    }

    public static void main(String[] args) {
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for (int i = 0; i < 1000000; i++) {
            if(random(n) < n/2)
                low++;
        }
        System.out.println("low = " + low);

        for (int i = 0; i < 1000000; i++) {
            System.out.println(rnd.nextInt(2));
        }
    }
}
