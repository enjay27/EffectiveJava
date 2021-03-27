## 아이템 77. 예외를 무시하지 말라

### 1. 아무것도 하지 않는 catch
```java
try{
    (...)
} catch(SomeException e) {
}
```
이 경우 예외가 무시되어 예외가 존재할 이유가 없어진다. 

---

다만 FileInputStream.close() 처럼 예외가 필요 없는 경우도 있다. 남은 작업을 중단할 필요도 없다.

예외를 무시하기로 했으면 catch 블록 안에 주석을 남기고 변수 이름도 ignored 로 바꿔놓도록 하자.

```java
try{
    fileInputStream.close();
} catch(IOException ignored) {
    //작업 끝내고 닫을거임    
}
```