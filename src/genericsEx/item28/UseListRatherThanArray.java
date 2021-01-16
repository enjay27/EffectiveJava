package genericsEx.item28;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UseListRatherThanArray {
    Object[] objectArray;
    List<Object> objectList;

    public void usingArray() {
        objectArray = new Long[1];
        objectArray[0] = "Different Type"; // 컴파일 성공, 런타임 에러 ArrayStoreException
    }

    public void usingList() {
        //objectList = new ArrayList<Long>(); // 컴파일 에러
    }


}
