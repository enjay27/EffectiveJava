package lambdaEx.item44;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;


public class UseStandardFunctionalInterface<K, V> {

    public static void main(String[] args) {

    }

    public void test() {
        BiPredicate<Map<K, V>,Map.Entry<K, V>> remove = (map,  eldest) -> {
            return false;
        };
    }




    private int size = 0;

    private int size() {
        return size;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > 100;
    }

    @FunctionalInterface
    interface EldestEntryRemovalFunction<K, V> {
        boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
    }

}
