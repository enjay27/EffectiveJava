## 아이템 75. 예외의 상세 메시지에 실패 관련 정보를 담으라

### 1. Stack Trace

프로그램이 실패하면 실패 원인에 대한 정보를 가능한 한 많이 담아 반환해야 한다. 실패 순간을 포착하려면 예외에 관여된 모든 매개변수와 필드의 값을 실패 메시지에 담아야 한다.
   
IndexOutOfBoundsException 의 메시지는 범위의 최소, 최댓값, 범위를 벗어난 인덱스의 값을 담아야 한다. 이 정보를 통해 어느 것이 잘못됐는 지 알 수 있다.

다만 최종 사용자에게는 친절한 안내 메시지를 보여줘야 한다. 


```java
/**
* IndexOutOfBoundsException
*
* @param lowerBound 인덱스의 최솟값
* @param upperBound 인덱스의 최댓값 + 1
* @param index 인덱스의 실제 값
*/
public IndexOutOfBoundsException(int lowerBound, int upper Bound, int index) {
    //상태 메시지
    super(String.format(
            "최솟값: %d, 최댓값: %d, 인덱스: %d",
            lowerBound, upperBound, index));
    //실패 정보
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.index = index;
}
```
이렇게 만들면 예외가 최댓값, 최솟값, 인덱스의 정보를 모두 갖게 된다. 하지만 현재 Java 에서는 index 정보만 받을 수 있다. 
