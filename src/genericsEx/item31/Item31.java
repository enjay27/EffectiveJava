package genericsEx.item31;

import java.util.ArrayList;
import java.util.Collection;


public class Item31 {

    public static void main(String[] args) {
        UseUnboundedWildCard<Number> numberStack = new UseUnboundedWildCard<>();
        Iterable<Integer> integers = new ArrayList<>();
        numberStack.pushAll(integers);

        Collection<Object> objects = new ArrayList<>();
        //numberStack.popAll(objects); //컴파일에러
    }
}
