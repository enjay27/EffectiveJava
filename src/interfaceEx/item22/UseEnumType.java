package interfaceEx.item22;

public enum UseEnumType {
    PI(3.141592),
    AVOGADRO_NUMBER(6.022e+23),
    ELECTRON_MASS(9.109_383_56e-31);

    public final Double value;

    private UseEnumType(double num) {
        this.value = num;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
}
