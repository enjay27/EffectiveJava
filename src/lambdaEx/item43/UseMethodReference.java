package lambdaEx.item43;

import enumEx.item38.ExtendedOperation;

import java.util.HashMap;
import java.util.Map;

public class UseMethodReference {
    public static void main(String[] args) {
        Map<String, Long> map = new HashMap<>();
        String key = "123";
        map.merge(key, 1L, (count, incr) -> count + incr);
        map.merge(key, 1L, Long::sum);

        UseMethodReference service = new UseMethodReference();

        service.execute(() -> action());
        service.execute(UseMethodReference::action);


    }

    public static void action() {

    }

    public void execute(Runnable r) {

    }

    interface G1 {
        <E extends Exception> Object m() throws E;
    }

    interface G2 {
        <E extends Exception> String m() throws Exception;
    }

    interface G extends G1, G2 {

    }

}
