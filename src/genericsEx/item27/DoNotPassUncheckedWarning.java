package genericsEx.item27;

import java.util.ArrayList;
import java.util.Arrays;

public class DoNotPassUncheckedWarning<T> {

    int size = 0;
    transient Object[] elementData; // non-private to simplify nested class access


    public <T> T[] toArray(T[] a) {
        if (a.length < size)
        {
            //생성한 배열과 매개변수로 받은 배열이 모두 T[]로 같다.
            @SuppressWarnings("unchecked") T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
            return result;
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
}
