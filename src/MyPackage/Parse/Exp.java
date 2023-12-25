package MyPackage.Parse;


import MyPackage.IR.Value;

public class Exp {
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public int getLevel() {
        return addExp.getLevel();
    }

    public Value generateLlvm() {
        Value value = addExp.generateLlvm();
        return value;
    }

    public int getValue() {
        return addExp.getValue();
    }
}
