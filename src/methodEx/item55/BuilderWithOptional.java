package methodEx.item55;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class BuilderWithOptional {

    private final int neededValue1;
    private final int neededValue2;
    private final OptionalInt notNeededValue1;
    private final OptionalInt notNeededValue2;
    private final OptionalInt notNeededValue3;

    public BuilderWithOptional(Builder builder) {
        this.neededValue1 = builder.neededValue1;
        this.neededValue2 = builder.neededValue2;
        this.notNeededValue1 = builder.notNeededValue1;
        this.notNeededValue2 = builder.notNeededValue2;
        this.notNeededValue3 = builder.notNeededValue3;
    }

    public static class Builder {
        private final int neededValue1;
        private final int neededValue2;
        private OptionalInt notNeededValue1 = OptionalInt.empty();
        private OptionalInt notNeededValue2 = OptionalInt.empty();
        private OptionalInt notNeededValue3 = OptionalInt.empty();

        public Builder(int neededValue1, int neededValue2) {
            this.neededValue1 = neededValue1;
            this.neededValue2 = neededValue2;
        }

        public Builder value1(Integer val) {
            notNeededValue1 = OptionalInt.of(val);
            return this;
        }
        public Builder value2(Integer val) {
            notNeededValue2 = OptionalInt.of(val);
            return this;
        }
        public Builder value3(Integer val) {
            notNeededValue3 = OptionalInt.of(val);
            return this;
        }

        public BuilderWithOptional build() {
            return new BuilderWithOptional(this);
        }
    }

    public int getNeededValue1() {
        return neededValue1;
    }

    public int getNeededValue2() {
        return neededValue2;
    }

    public OptionalInt getNotNeededValue1() {
        return notNeededValue1;
    }

    public OptionalInt getNotNeededValue2() {
        return notNeededValue2;
    }

    public OptionalInt getNotNeededValue3() {
        return notNeededValue3;
    }
}
