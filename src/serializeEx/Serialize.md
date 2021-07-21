# 직렬화 (Serialization)

> 객체를 데이터 스트림으로 만드는 것   
> 객체에 저장된 데이터를 쓰기 위해 연속적인 데이터로 변환하는 것

## 왜 사용해야 할까?

메모리에 있는 객체 데이터를 그대로 영속화(Persistence) 할 때 사용된다.

이렇게 영속화된 데이터는 네트워크로 전송도 가능해진다.

또한 필요할 때 직렬화된 객체 데이터를 가져와서 역직렬화하여 객체를 바로 사용할 수 있게 한다.

## 어디서 사용될까?
 - 서블릿 세션
 - 캐시 (Cache)
 - 자바 RMI (Remote Method Invocation)

## 어떻게 사용하지?

우선 직렬화 대상 클래스를 직렬화가 가능하도록 만들어야 한다.

방법은 간단하다. 해당 클래스 혹은 상위 클래스가 Serializable 을 구현하고 있으면 된다. 

```java
class User implements Serializable {
    String name;
    String password;
    ....
}
```

상위 클래스에서 상속을 받은 경우, 그리고 하위 클래스에서만 Serializable 을 구현 한 경우는   
상위 클래스의 인스턴스변수는 제외한 채 하위 클래스의 인스턴스변수만 직렬화된다.

```java
class SuperUser {
    String name;
}
class User extends SuperUser implements Serializable {
    String password;
}
```

Object 객체는 Serializable 을 구현하지 않았기 때문에 직렬화할 수 없다.

```java
class User implements Serializable {
    String name;
    String password;
    Object obj = new Object(); // 직렬화 불가능
}
```

직렬화 대상에서 제외하려는 경우 앞에 transient 제어자를 붙인다.

```java
class User implements Serializable {
    String name;
    transient String password; // 직렬화 대상에서 제외
}
```
---
다음으로 직렬화, 역직렬화 실행 객체인 ObjectInputStream, ObjectOutputStream 을 사용해 직렬화, 역직렬화 한다.

```java
Member member = new Member("김배민", "deliverykim@baemin.com", 25);
byte[] serializedMember;
try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
        oos.writeObject(member);
        // serializedMember -> 직렬화된 member 객체 
        serializedMember = baos.toByteArray();
    }
}
// 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
System.out.println(Base64.getEncoder().encodeToString(serializedMember));
```

```java
// 직렬화 예제에서 생성된 base64 데이터 
String base64Member = "...생략";
byte[] serializedMember = Base64.getDecoder().decode(base64Member);
try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
        // 역직렬화된 Member 객체를 읽어온다.
        Object objectMember = ois.readObject();
        Member member = (Member) objectMember;
        System.out.println(member);
    }
}
```

## Java 말고도 쓴다던데?

> 대표적으로 JSON, CSV 가 있다.

### JSON

Javascript 에서 쉽게 사용 가능하고, 다른 데이터 포맷 방식에 비해 오버헤드가 적다.

```json
{ 
  name: "김배민",
  email: "deliverykim@baemin.com",
  age: 25 
}
```

### CSV

Comma Seperated Value

콤마를 기준으로 데이터를 구분하는 방식으로 가장 많이 사용된다.

## 장점?

![img](https://techblog.woowahan.com/wp-content/uploads/img/2017-10-05/java-serial-ex1.jpeg)

다른 시스템 간의 데이터 교환은 JSON 이나 CSV 혹은 다른 기타 직렬화 방법을 사용할 수 있지만, 

자바 직렬화는 자바에 최적화되어 있는 만큼 자바 시스템 간의 데이터 교환은 자바 직렬화를 사용하는 것이 좋다.


단점은 다음시간에...


---

- 참조 : [자바 직렬화, 그것이 알고싶다.](https://techblog.woowahan.com/2550/) 