package methodEx.item54;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReturnEmptyCollection {

    public static void main(String[] args) {
        ReturnEmptyCollection r = new ReturnEmptyCollection();
        List<Integer> integers = r.getIntegers();
        System.out.println("integers = " + integers + integers.get(2));

    }

    private final List<Integer> integers;

    private static final Integer[] EMPTY_INTEGER_ARRAY = new Integer[0];

    public Integer[] getIntegerArray() {
        return integers.toArray(EMPTY_INTEGER_ARRAY);
    }

    public ReturnEmptyCollection() {
        integers = new ArrayList<>();
    }

    public List<Integer> getIntegersWithNull() {
        return integers.isEmpty() ? null
                : new ArrayList<>(integers);
    }

    public List<Integer> getIntegers() {
        return integers.isEmpty() ? Collections.emptyList()
                : new ArrayList<>(integers);
    }

    public void addIntegers(Integer number) {
        integers.add(number);
    }
}
