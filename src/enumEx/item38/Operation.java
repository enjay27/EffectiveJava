package enumEx.item38;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operation {
    public final static UseInterface[] operation = Stream.concat(
            Arrays.stream(BasicOperation.values()), Arrays.stream(ExtendedOperation.values())
    ).toArray(UseInterface[]::new);

    public static final List<UseInterface> operationList = new ArrayList<>();;

    public Operation() {
        operationList.addAll(Arrays.asList(BasicOperation.values().clone()));
        operationList.addAll(Arrays.asList(ExtendedOperation.values().clone()));
    }

    enum Operations {
        BASIC(BasicOperation.values()),
        EXTENDED(ExtendedOperation.values());

        final UseInterface[] operations;

        Operations(UseInterface[] operation) {
            this.operations = operation;
        }
    }
}
