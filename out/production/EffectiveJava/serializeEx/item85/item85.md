## 아이템 85. 자바 직렬화의 대안을 찾으라

### 직렬화의 위험
분산 객체를 만들 수 있게 구현한 기능이 보이지 않는 생성자, API와 구현 사이의 모호해진 경계, 
잠재적인 정황성 문제, 성능, 보안, 유지보수성 문제를 일으켰다.

ObjectInputStream 의 readObject 메서드를 호출하면서 객체 그래프가 역직렬화 된다. 
이 과정에서 readObject 메서드는 그 타입들 안의 모든 코드를 수행할 수 있다.

```java
static byte[] bomb() {
    Set<Object> root = new HashSet<>();
    Set<Object> s1 = root;
    Set<Object> s2 = new HashSet<>();
    for (int i = 0; i < 100; i++) {
        Set<Object> t1 = new HashSet<>();
        Set<Object> t2 = new HashSet<>();
        t1.add("foo"); // make it not equal to t2
        s1.add(t1);
        s1.add(t2);
        s2.add(t1);
        s2.add(t2);
        s1 = t1;
        s2 = t2;
    }
    return serialize(root);
}
```
이 메서드에서 HashSet 인스턴스를 역직렬화하려면 그 원소들의 해시코드를 계산해야한다.    
반복문에 의해 루트 아래에 100단계까지 만들어진다.    
이 HashSet을 역직렬화 하려면 hashCode 메서드를 엄청나게 많이 호출해야 한다.   

### 자바 직렬화 대안
자바 직렬화 대신 객체와 바이트 시퀀스를 변환해주는 다른 메커니즘들을 사용한다.    
크로스-플랫폼 구조화된 데이터 표현(cross-platform structured-data representation) 사용

- JSON : Javascript용 텍스트 기반. 
- Protocol Buffers(protobuf) : C++용 이진 표현. 효율이 좋다.


#### 신뢰할 수 없는 데이터는 절대 역직렬화하지 않는다.
자바 9에 역직렬화 필터링이 추가되었다. 특정 클래스를 받아들이거나 거부할 수 있다.   
블랙리스트, 화이트리스트 방식이 가능하다.

