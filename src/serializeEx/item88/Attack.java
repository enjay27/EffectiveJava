package serializeEx.item88;

import java.util.Date;

public class Attack {
    public static void main(String[] args) {
        MutablePeriod mp = new MutablePeriod();
        Period p = mp.period;
        Date pEnd = mp.end;

        // return time
        pEnd.setYear(78);
        System.out.println("p = " + p);

        // return 69
        pEnd.setYear(69);
        System.out.println("p = " + p);
    }
}
