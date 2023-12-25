package MyPackage.Parse;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.IRModule;
import MyPackage.IR.Type;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class FuncFParam {
    private String type;
    private String ident;
    private ArrayList<ConstExp> constExps;
    private int level;

    public FuncFParam(String type, String ident, ArrayList<ConstExp> constExps,int level) {
        this.type = type;
        this.ident = ident;
        this.constExps = constExps;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void generateLlvm() {
        if (level == 0) {
            int id = IRModule.getRegID();
            IRModule.curFunction.addParams(new Value(Type.Reg, id), ident);
        }
        else if (level == 1){
            int id = IRModule.getRegID();
            IRModule.curFunction.addParams(new Value(Type.Pointer, id), ident);
        }
        else {
            int id = IRModule.getRegID();
            ArrayLlvm arrayLlvm = new ArrayLlvm(Type.Pointer, id);
            for (int i = 0; i < constExps.size(); i++) {
                arrayLlvm.addDim(constExps.get(i).getValue());
            }
            IRModule.curFunction.addParams(arrayLlvm, ident);
        }
    }
}
