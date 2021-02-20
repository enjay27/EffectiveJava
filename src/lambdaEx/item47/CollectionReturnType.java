package lambdaEx.item47;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionReturnType {

    public static void main(String[] args) {
        for (ProcessHandle ph : (Iterable<? extends ProcessHandle>) ProcessHandle.allProcesses()::iterator){

        }

        for (ProcessHandle ph : iterableOf(ProcessHandle.allProcesses())) {

        }

        streamOf(ProcessHandle.allProcesses().collect(Collectors.toSet()));

        List<Integer> integers = new ArrayList<>();
        streamOf(integers);

        Set<Integer> integerSet = new HashSet<>();
        Collection<Set<Integer>> of = of(integerSet);
        for (Set<Integer> s : of) {

        }

    }

    public static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    public static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream((iterable.spliterator()), false);
    }

    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s);
        if (src.size() > 30)
            throw new IllegalArgumentException("Too many elements." + s);

        return new AbstractList<Set<E>>() {

            @Override
            public int size() {
                return 1 << src.size();
            }

            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index != 0; index >>= 1)
                    if ((index & 1) == 1)
                        result.add(src.get(i));
                return result;
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set)o);
            }
        };
    }
}
