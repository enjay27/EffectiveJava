package genericsEx.item26;

import java.lang.reflect.Array;
import java.util.*;

public class DoNotUseRawType {

    private static final Collection stamps = new ArrayList();
    private static final Collection<Stamp> stampsWithGenerics = new ArrayList<>();

    public static Collection getStamps() {
        return stamps;
    }

    public static Collection<Stamp> getStampsWithGenerics() {
        return stampsWithGenerics;
    }

    static class Stamp {

    }

    static class Coin {

    }

    public void useRawTypeParameter(Set s1, Set s2) {
        s1.add(1000);
        s2.add("RAWTYPE");
    }

    public void useUnboundedWildTypeParameter(Set<?> s1, Set<?> s2) {
        //s1.add(1000); //컴파일에러
        useClassLiteral();
    }

    public void useClassLiteral() {
        Map<Class<?>, Object> map = new HashMap<>();
        map.put(String.class, "12323");
        map.put(Integer.class, 12323);
        map.put(List.class, new ArrayList<>());
        //map.put(List<Integer>.class, new ArrayList<>()); //이렇게는 안 쓴다
    }

    public boolean useInstanceOf(Set<?> s1) {
        //return s1 instanceof Set<Integer>; //컴파일 에러
        return s1 instanceof Set<?> && s1 instanceof Set;
    }
}
