package MyPackage.Parse;

import MyPackage.IR.Value;

import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps;

    public FuncRParams(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    public int getLevel() {
        return exps.size();
    }

    public void generateLlvm(ArrayList<Value> values) {
        for (int i = 0; i < exps.size(); i++) {
            values.add(exps.get(i).generateLlvm());
        }
    }
}
