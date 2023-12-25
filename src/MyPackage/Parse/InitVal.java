package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;

import java.util.ArrayList;

public class InitVal {
    private Exp exp;
    private int type;
    private ArrayList<InitVal> initValS;

    public InitVal(Exp exp) {
        this.exp = exp;
        type = 0;
    }

    public InitVal(ArrayList<InitVal> initValS) {
        this.initValS = initValS;
        type = 1;
    }

    public ArrayList<InitVal> getInitValS() {
        return initValS;
    }

    public void generateLlvm(ArrayList<Integer> list) {
        if (type == 0) {
            list.add(exp.getValue());
        }
        else {
            for (int i = 0; i < initValS.size(); i++) {
                initValS.get(i).generateLlvm(list);
            }
        }
    }

    public void generateLlvm2(ArrayList<Value> list) {
        if (type == 0) {
            list.add(exp.generateLlvm());
        }
        else {
            for (int i = 0; i < initValS.size(); i++) {
                initValS.get(i).generateLlvm2(list);
            }
        }
    }
}
