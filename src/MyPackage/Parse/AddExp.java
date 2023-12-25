package MyPackage.Parse;

import MyPackage.IR.Instruction.AddLlvm;
import MyPackage.IR.Instruction.SubLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.OutPut;
import MyPackage.IR.IRModule;

import java.util.ArrayList;

public class AddExp {
    private ArrayList<MulExp> mulExps;
    private ArrayList<String> op;


    public AddExp(ArrayList<MulExp> mulExps, ArrayList<String> op) {
        this.mulExps = mulExps;
        this.op = op;
    }

    public int getLevel() {
        if (mulExps.size() > 1) {
            return 0;
        }
        return mulExps.get(0).getLevel();
    }

    public Value generateLlvm() {
        int value = 0;
        Value reg = null;
        Value pair = mulExps.get(0).generateLlvm();
        if (pair.getType().equals(Type.MyConst)) {
            value = pair.getValue();
        }
        else {
            reg = pair;
        }
        for (int i = 1; i < mulExps.size(); i++) {
            pair = mulExps.get(i).generateLlvm();
            if (op.get(i - 1).equals("+")) {
                if (pair.getType().equals(Type.MyConst)) {
                    value += pair.getValue();
                }
                else {
                    if (reg != null) {
                        int regId = IRModule.getRegID();
                        AddLlvm addLlvm = new AddLlvm(Type.Reg, regId);
                        addLlvm.addOperand(reg);
                        reg.addUse(new Use(reg, addLlvm, addLlvm.getPos()));
                        addLlvm.addOperand(pair);
                        pair.addUse(new Use(pair, addLlvm, addLlvm.getPos()));
                        IRModule.curFunction.getCurrentBlock().addInstruction(addLlvm);
                        reg = addLlvm;
                    }
                    else {
                        reg = pair;
                    }
                }
            }
            else {
                if (pair.getType().equals(Type.MyConst)) {
                    value -= pair.getValue();
                } else {
                    int regId = IRModule.getRegID();
                    SubLlvm subLlvm = new SubLlvm(Type.Reg, regId);
                    if (reg == null) {
                        reg = new Value(Type.MyConst, 0);
                    }
                    subLlvm.addOperand(reg);
                    subLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, subLlvm, subLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(subLlvm);
                    reg = subLlvm;
                }
            }
        }
        if (reg == null) {
            return new Value(Type.MyConst, value);
        }
        else if (value != 0) {
            int id = IRModule.getRegID();
            AddLlvm addLlvm = new AddLlvm(Type.Reg, id);
            Value con = new Value(Type.MyConst, value);
            addLlvm.addOperand(con);
            addLlvm.addOperand(reg);
            reg.addUse(new Use(reg, addLlvm, addLlvm.getPos()));
            IRModule.curFunction.getCurrentBlock().addInstruction(addLlvm);
            return addLlvm;
        }
        return reg;
    }


    public int getValue() {
        int value = mulExps.get(0).getValue();
        for (int i = 1; i < mulExps.size(); i++) {
            if (op.get(i - 1).equals("+")) {
                value += mulExps.get(i).getValue();
            }
            else {
                value -= mulExps.get(i).getValue();
            }
        }
        return value;
    }
}
