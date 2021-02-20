package lambdaEx.item46;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SideEffect {

    public static void main(String[] args) {
        File file = new File("a.txt");
        Map<String, Long> freq = new HashMap<>();
        try (Stream<String> words = new Scanner(file).tokens()) {
            words.forEach(word -> {
                freq.merge(word.toLowerCase(), 1L, Long::sum);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Map<String, Long> freq2;
        try (Stream<String> words = new Scanner(file).tokens()) {
            freq2 = words
                    .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
