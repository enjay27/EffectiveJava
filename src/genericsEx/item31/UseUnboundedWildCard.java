package genericsEx.item31;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class UseUnboundedWildCard<E> {

    E element;

    public void pushAll(Iterable<? extends E> src) {
        src.forEach(this::push);
    }

    public void push(Object e){};

    public void popAll(Collection<? super E> dst) {
        while(isEmpty())
            dst.add(pop());
    }

    private boolean isEmpty() {
        return true;
    }

    private E pop() {
        return element;
    }

    public static void swapWildCard(List<?> list, int i, int j){
        swapHelper(list, i, j);
    }
    public static <E> void swapHelper(List<E> list, int i, int j){
        list.set(i, list.set(j, list.get(i)));
    }
}
