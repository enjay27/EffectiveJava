package lambdaEx.item48;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CarefulWithStreamParallelize {
    public static <T> void main(String[] args) {
        Stream<BigInteger> bigIntegerStream = primes();
        bigIntegerStream.map(p -> BigInteger.TWO.pow(p.intValueExact()).subtract(BigInteger.ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .parallel()
                .forEach(System.out::println);

        List<Author> authors = new ArrayList<>();
        authors.add(new Author("A", 1));
        authors.add(new Author("B", 2));
        authors.add(new Author("C", 3));
        authors.add(new Author("D", 4));
        authors.add(new Author("E", 5));

        RelatedAuthorSpliterator relatedAuthorSpliterator = new RelatedAuthorSpliterator(authors);
        Stream<Author> authorStream = StreamSupport.stream(relatedAuthorSpliterator, false);
        authorStream.parallel().forEach(System.out::println);
    }

    static Stream<BigInteger> primes() {
        return Stream.iterate(BigInteger.TWO, BigInteger::nextProbablePrime);
    }

    static class PrimeSpliterator<T extends Number> implements Spliterator<Number> {

        List<Number> numbers;
        int certainty;
        int size;
        AtomicInteger current = new AtomicInteger();
        Iterator<Number> it;

        public PrimeSpliterator(int certainty, int size) {
            this.certainty = certainty;
            this.size = size;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Number> action) {
            if (action == null)
                throw new NullPointerException();
            if (current.get() >= 0 && current.get() < certainty) {
                action.accept(numbers.get(current.getAndIncrement()));
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Number> trySplit() {
            int currentSize = certainty - current.get();

            if (currentSize < 4) {
                return null;
            } else {
//                return new PrimeSpliterator<T>();
            }

//            for (int splitPos = currentSize / 2 + current.intValue();
//                 splitPos < numbers.size(); splitPos++) {
//                if (numbers.get(splitPos).getRelatedArticleId() == 0) {
//                    Spliterator<T> spliterator
//                            = new RelatedAuthorSpliterator(
//                            numbers.subList(current.get(), splitPos));
//                    current.set(splitPos);
//                    return spliterator;
//                }
//            }
            return null;
        }

        @Override
        public long estimateSize() {
            return 10L; //numbers.size() - current.get();
        }

        @Override
        public int characteristics() {
            return CONCURRENT;
        }

    }

}

