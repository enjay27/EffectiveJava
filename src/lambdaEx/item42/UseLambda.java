package lambdaEx.item42;

import java.util.*;

public class UseLambda {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>();

        Collections.sort(words, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });

        Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));

        Collections.sort(words, Comparator.comparingInt(String::length));
        words.sort(Comparator.comparingInt(String::length));
    }
}
