package enumEx.item37;

import java.util.ArrayList;
import java.util.List;

public class UseEnumMap {
    enum LifeCycle { ANNUAL, PERENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    public static List<UseEnumMap> garden = new ArrayList<>();;
    public static UseEnumMap[] arrayGarden;

    {
        garden.add(new UseEnumMap("aa", LifeCycle.ANNUAL));
        garden.add(new UseEnumMap("bb", LifeCycle.ANNUAL));
        garden.add(new UseEnumMap("cc", LifeCycle.PERENNIAL));
        garden.add(new UseEnumMap("dd", LifeCycle.BIENNIAL));
        garden.add(new UseEnumMap("ee", LifeCycle.BIENNIAL));

        arrayGarden[0] = new UseEnumMap("aa", LifeCycle.ANNUAL);
        arrayGarden[1] = new UseEnumMap("bb", LifeCycle.ANNUAL);
        arrayGarden[2] = new UseEnumMap("cc", LifeCycle.PERENNIAL);
        arrayGarden[3] = new UseEnumMap("dd", LifeCycle.BIENNIAL);
        arrayGarden[4] = new UseEnumMap("ee", LifeCycle.BIENNIAL);
    }

    public UseEnumMap(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }


}

