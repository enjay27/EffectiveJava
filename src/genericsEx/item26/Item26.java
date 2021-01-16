package genericsEx.item26;

public class Item26 {
    public static void main(String[] args) {
        DoNotUseRawType.getStamps().add(new DoNotUseRawType.Coin());
        //DoNotUseRawType.getStampsWithGenerics().add(new DoNotUseRawType.Coin());
    }
}
