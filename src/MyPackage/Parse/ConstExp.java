package MyPackage.Parse;

import MyPackage.IR.Value;

public class ConstExp {
    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public Value generateLlvm() {
        return addExp.generateLlvm();
    }

    public int getValue() {
        return addExp.getValue();
    }
}
