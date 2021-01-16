package classEx.item24;

public class UseStaticMemberClass {

    public void usingAnonymousClass() {
        AnonymousClass a = new AnonymousClass() {
            @Override
            public void abstractMethod() {
            }
        };

        a.abstractMethod();

    }

    public void usingMemberClass() {
        StaticMemberClass s = new StaticMemberClass();
        s.staticMemberClassMethod();

        MemberClass m = new MemberClass();
        m.MemberClassMethod();

    }

    public void usingLambda() {
        LambdaExpression l = System.out::println;
        l.LambdaMethod();

        Runnable r = System.out::println;
        r.run();
    }

    public void usingLocalClass() {
        class LocalClass {
            public void localClassMethod() {

            }
        }
        LocalClass l = new LocalClass();
        l.localClassMethod();
    }

    static class StaticMemberClass {
        public void staticMemberClassMethod(){}
    }

    class MemberClass {
        public void MemberClassMethod(){}
    }

}

abstract class AnonymousClass {

    abstract public void abstractMethod();
}

@FunctionalInterface
interface LambdaExpression {
    public void LambdaMethod();
}