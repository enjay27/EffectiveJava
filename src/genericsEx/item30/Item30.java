package genericsEx.item30;

import java.util.Set;

public class Item30 {
    public static void main(String[] args) {
        Set<String> s1 = Set.of("12", "34", "56");
        Set<String> s2 = Set.of("aa", "bb", "cc");
        Set<String> s3 = MakeGenericMethod.unionUsingGeneric(s1, s2);
        System.out.println(s3);
    }
}
