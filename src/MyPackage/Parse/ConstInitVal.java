package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;

import java.util.ArrayList;

public class ConstInitVal {
    private ConstExp constExp;
    private ArrayList<ConstInitVal> constInitValS;
    private int type;


    public ConstInitVal(ConstExp constExp) {
        this.constExp = constExp;
        type = 0;
    }

    public ConstInitVal(ArrayList<ConstInitVal> constInitValS) {
        this.constInitValS = constInitValS;
        type = 1;
    }

    public void generateLlvm(ArrayList<Integer> list) {
        if (type == 0) {
            list.add(constExp.getValue());
        }
        else {
            for (int i = 0; i < constInitValS.size(); i++) {
                constInitValS.get(i).generateLlvm(list);
            }
        }
    }


    public void generateLlvm2(ArrayList<Value> values) {
        if (type == 0) {
            values.add(constExp.generateLlvm());
        }
        else {
            for (int i = 0; i < constInitValS.size(); i++) {
                constInitValS.get(i).generateLlvm2(values);
            }
        }
    }
}
