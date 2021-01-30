package enumEx.item37;

import java.util.*;
import java.util.stream.Collectors;

public class Item37 {
    public static void main(String[] args) {


        Map<UseEnumMap.LifeCycle, Set<UseEnumMap>> plantsByLifeCycle =
                new EnumMap<>(UseEnumMap.LifeCycle.class);
        for (UseEnumMap.LifeCycle lc : UseEnumMap.LifeCycle.values()) {
            plantsByLifeCycle.put(lc, new HashSet<>());
        }
        for (UseEnumMap u : UseEnumMap.garden) {
            plantsByLifeCycle.get(u.lifeCycle).add(u);
        }
        System.out.println(plantsByLifeCycle);

        System.out.println(UseEnumMap.garden.stream()
                .collect(Collectors.groupingBy(p -> p.lifeCycle,
                        () -> new EnumMap<>(UseEnumMap.LifeCycle.class), Collectors.toSet())));
    }
}
