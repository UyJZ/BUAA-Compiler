package MyPackage.Parse;

import MyPackage.IR.IRModule;

import java.util.ArrayList;

public class LOrExp {
    private ArrayList<LAndExp> lAndExps;

    public LOrExp(ArrayList<LAndExp> lAndExps) {
        this.lAndExps = lAndExps;
    }

    public void generateLlvm(int id) {
        for (int i = 0; i < lAndExps.size(); i++) {
            if (i != 0) {
                IRModule.curFunction.newBlock(String.format("cond_%d_%d",
                        id, i));
            }
            lAndExps.get(i).generateLlvm(i, lAndExps.size(), id);
        }
    }
}
