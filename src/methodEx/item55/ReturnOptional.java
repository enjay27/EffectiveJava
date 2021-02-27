package methodEx.item55;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReturnOptional {

    public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        if(c.isEmpty())
            return Optional.empty();
        E result = null;
        for (E e : c) {
            if( result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);
        }
        return Optional.of(result);
    }

    public static void main(String[] args) {

        Stream<Optional<Integer>> streamOfOptionals = Stream.of(
                Optional.of(10), Optional.of(20), Optional.empty(), Optional.of(30));
        Stream<Integer> integerStream = streamOfOptionals.flatMap(Optional::stream);
        integerStream.forEach(System.out::println);

        BuilderWithOptional builder = new BuilderWithOptional.Builder(10, 20).value1(15).value2(18).value3(210).build();
        Stream<OptionalInt> nullableStream = Stream.of(builder.getNotNeededValue1(), builder.getNotNeededValue2(),
                builder.getNotNeededValue3());
        IntStream intStream = nullableStream.flatMapToInt(OptionalInt::stream);
        intStream.forEach(System.out::println);
    }
}
