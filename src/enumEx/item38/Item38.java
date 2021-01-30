package enumEx.item38;

import java.util.Arrays;
import java.util.Collection;

public class Item38 {
    public static void main(String[] args) {
        double x = 10.2;
        double y = 5.3;
        test1(ExtendedOperation.class, x, y);
        test2(Arrays.asList(ExtendedOperation.values()), x, y);
        test1(BasicOperation.class, x, y);
    }

    private static <T extends Enum<T> & UseInterface> void test1(Class<T> opEnumType, double x, double y) {
        for (UseInterface op : opEnumType.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
        }
    }

    private static void test2(Collection<? extends UseInterface> opSet,
                              double x, double y) {
        for (UseInterface op : opSet) {
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
        }
    }
}
