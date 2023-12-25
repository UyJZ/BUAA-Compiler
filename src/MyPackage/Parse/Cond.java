package MyPackage.Parse;

import MyPackage.IR.Value;

public class Cond {
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public void generateLlvm(int id) {
        lOrExp.generateLlvm(id);
    }
}
