package interfaceEx.item21;

public class Item21 {
    public static void main(String[] args) {
        ThreadEx threadExA = new ThreadEx();
        ThreadEx threadExB = new ThreadEx();
        ThreadEx threadExC = new ThreadEx();
        ThreadEx threadExD = new ThreadEx();

        Thread threadA = new Thread(threadExA, "A");
        Thread threadB = new Thread(threadExA, "B");
        Thread threadC = new Thread(threadExA, "C");
        Thread threadD = new Thread(threadExA, "D");

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
    }
}
