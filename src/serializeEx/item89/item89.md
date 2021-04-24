## 아이템 89. 인스턴스 수를 통제해야 한다면 readResolve 보다는 열거 타입을 사용하라

### 싱글턴 직렬화 문제점
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    
    public void leaveTheBuilding() { ... }
}
```
바깥에서 생성자를 호출하지 못하게 막아서 인스턴스가 오직 하나만 만들어짐을 보장한다.

여기서 implements Serializable 을 추가하는 순간 싱글턴이 아니게 된다.    
이는 어떤 readObject를 사용하든 이 클래스가 초기화될 때 만들어진 인스턴스와는 별개인 인스턴스를 반환한다.


### readResolve

readResolve 기능을 이용하면 readObject 가 만들어낸 인스턴스를 다른 것으로 대체할 수 있다.   

역직렬화 후 새로 생성된 객체를 인수로 이 메서드가 호출되고, 이 메서드가 반환한 객체 참조가 새로 생성된 객체를 대신해 반환된다.

참조를 유지하지 않는 경우 가비지 컬렉션의 대상이 되기 때문에 싱글턴 클래스가 Serializable 을 구현하는 경우 readResolve 메서드를 추가해 싱글턴을 유지할 수 있다.

```java
private Object readResolve() {
    // 진짜만 반환하고 나머지는 가비지 컬렉터에 넘긴다.
    return INSTANCE;
}
```

### readResolve 의 문제점 without transient

__readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다.__   

싱글턴이 transient가 아닌 참조 필드를 가지고 있다면 그 필드는 readResolve 메서드가 실행되기 전 역직렬화 된다.

```java
import java.io.Serializable;

public class Elvis implements Serializable {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    
    private String[] favoriteSongs = { "AB", "CD" };
    
    private Object readResolve() {
        return INSTANCE;
    }
}
```

```java
import java.io.Serializable;

public class ElvisStealer implements Serializable {
    static Elvis impersonator;
    private Elvis payload;
    
    private Object readResolve() {
        // readSolve 되기 전 참조 저장
        impersonator = payload;
        
        return new String[] { "DD" };
    }
    private static final long serialVersionUID = 0;
}
```

도둑 클래스는 readSolve 메서드와 인스턴스 필드를 가지고 있다.    
직렬화된 Elvis 스트림에서 싱글턴의 필드를 이 도둑의 인스턴스로 교체한다.   
이러면 싱글턴은 도둑을 참조하고 도둑은 싱글턴을 참조하게 된다.

싱글턴이 역직렬화될 때 싱글턴이 도둑을 포함하기 때문에 도둑의 readResolve 메서드가 먼저 호출된다.   
결과 도둑의 readResolve 메서드 시점에 도둑의 인스턴스 필드에는 역직렬화 도중인 싱글턴의 참조가 담긴다.

이 참조를 정적필드 impersonator 에 복사하여 readResolve가 끝난 후에도 참조하게 한다.   
이후 Elvis 의 transient 가 아닌 필드의 원래 타입에 맞는 값을 반환하여 ClassCastException 을 피한다.

이렇게 하면 싱글턴 패턴으로 만들어 졌음에도 2개의 인스턴스가 생성될 수 있게 된다.

### enum 활용

원소 하나짜리 열거 타입으로 바꾸는 것이 좋다.(아이템 3)

열거 타입을 이용해 통제 클래스를 구현하면 선언한 상수 외의 다른 객체는 존재하지 않게 된다.   
AccessibleObject.setAccessible 같은 네이티브 코드를 수행할 수 있는 특권(privileged) 메서드는 제외하고.

```java
import java.util.*;

// Enum singleton - the preferred approach - Page 311
public enum Elvis {
    INSTANCE;
    private String[] favoriteSongs =
            { "Hound Dog", "Heartbreak Hotel" };
    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }
}
```
readResolve는 컴파일타임에는 어떤 인스턴스들이 있는지 알 수 없어,    
열거 타입으로 표현하는 것이 불가능한 상황에서 사용한다.


### readResolve 메서드의 접근자
final 클래스에서는 readResolve 메서드는 private 이어야 한다.   

final 이 아닌 클래스에서는 private 으로 선언하면 하위 클래스에서는 사용할 수 없다.   

default 로 선언하면 같은 패키지의 하위 클래스에서만 사용할 수 있다.   

protected, public 으로 선언하면 이를 재정의하지 않은 모든 하위 클래스에서 사용할 수 있다.
하위 클래스에서 재정의하지 않았다면, 하위 클래스의 인스턴스를 역직렬화하는 경우 
상위 클래스의 인스턴스를 생성하여 ClassCastException 을 일으킬 수 있다.