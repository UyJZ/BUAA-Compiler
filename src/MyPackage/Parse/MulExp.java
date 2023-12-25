package MyPackage.Parse;

import MyPackage.IR.Instruction.MulLlvm;
import MyPackage.IR.Instruction.SdivLlvm;
import MyPackage.IR.Instruction.SremLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.IR.IRModule;

import java.util.ArrayList;

public class MulExp {
    private ArrayList<UnaryExp> unaryExps;
    private ArrayList<String> op;

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<String> op) {
        this.unaryExps = unaryExps;
        this.op = op;
    }

    public int getLevel() {
        if (unaryExps.size() > 1) {
            return 0;
        }
        return unaryExps.get(0).getLevel();
    }

    public Value generateLlvm() {
        int value = 1;
        Value reg = null;
        Value pair = unaryExps.get(0).generateLlvm(1);
        if (pair.getType().equals(Type.MyConst)) {
            value = pair.getValue();
        }
        else {
            reg = pair;
        }
        for (int i = 1; i < unaryExps.size(); i++) {
            pair = unaryExps.get(i).generateLlvm(1);
            if (op.get(i - 1).equals("*")) {
                if (pair.getType().equals(Type.MyConst)) {
                    value *= pair.getValue();
                }
                else {
                    if (reg != null) {
                        int regId = IRModule.getRegID();
                        MulLlvm mulLlvm = new MulLlvm(Type.Reg, regId);
                        mulLlvm.addOperand(reg);
                        reg.addUse(new Use(reg, mulLlvm, mulLlvm.getPos()));
                        mulLlvm.addOperand(pair);
                        pair.addUse(new Use(pair, mulLlvm, mulLlvm.getPos()));
                        IRModule.curFunction.getCurrentBlock().addInstruction(mulLlvm);
                        reg = mulLlvm;
                    }
                    else {
                        reg = pair;
                    }
                }
            }
            else if (op.get(i - 1).equals("%")){
                if (reg == null && pair.getType().equals(Type.MyConst)) {
                    value %= pair.getValue();
                    continue;
                }
                if (value != 1 && reg != null) {
                    int regId = IRModule.getRegID();
                    MulLlvm mulLlvm = new MulLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, value);
                    mulLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, mulLlvm, mulLlvm.getPos()));
                    mulLlvm.addOperand(con);
                    IRModule.curFunction.getCurrentBlock().addInstruction(mulLlvm);
                    reg = mulLlvm;
                    value = 1;
                }
                if (reg == null) {
                    int regId = IRModule.getRegID();
                    SremLlvm sremLlvm = new SremLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, value);
                    sremLlvm.addOperand(con);
                    sremLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, sremLlvm, sremLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(sremLlvm);
                    reg = sremLlvm;
                    value = 1;
                }
                else if (pair.getType().equals(Type.MyConst)) {
                    int regId = IRModule.getRegID();
                    SremLlvm sremLlvm = new SremLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, pair.getValue());
                    sremLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, sremLlvm, sremLlvm.getPos()));
                    sremLlvm.addOperand(con);
                    IRModule.curFunction.getCurrentBlock().addInstruction(sremLlvm);
                    reg = sremLlvm;
                }
                else {
                    int regId = IRModule.getRegID();
                    SremLlvm sremLlvm = new SremLlvm(Type.Reg, regId);
                    sremLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, sremLlvm, sremLlvm.getPos()));
                    sremLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, sremLlvm, sremLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(sremLlvm);
                    reg = sremLlvm;
                }
            }
            else if (op.get(i - 1).equals("/")){
                if (reg == null && pair.getType().equals(Type.MyConst)) {
                    value /= pair.getValue();
                    continue;
                }
                if (value != 1 && reg != null) {
                    int regId = IRModule.getRegID();
                    MulLlvm mulLlvm = new MulLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, value);
                    mulLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, mulLlvm, mulLlvm.getPos()));
                    mulLlvm.addOperand(con);
                    IRModule.curFunction.getCurrentBlock().addInstruction(mulLlvm);
                    reg = mulLlvm;
                    value = 1;
                }
                if (reg == null) {
                    int regId = IRModule.getRegID();
                    SdivLlvm sdivLlvm = new SdivLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, value);
                    sdivLlvm.addOperand(con);
                    sdivLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, sdivLlvm, sdivLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(sdivLlvm);
                    reg = sdivLlvm;
                    value = 1;
                }
                else if (pair.getType().equals(Type.MyConst)) {
                    int regId = IRModule.getRegID();
                    SdivLlvm sdivLlvm = new SdivLlvm(Type.Reg, regId);
                    Value con = new Value(Type.MyConst, pair.getValue());
                    sdivLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, sdivLlvm,sdivLlvm.getPos()));
                    sdivLlvm.addOperand(con);
                    IRModule.curFunction.getCurrentBlock().addInstruction(sdivLlvm);
                    reg = sdivLlvm;
                }
                else {
                    int regId = IRModule.getRegID();
                    SdivLlvm sdivLlvm = new SdivLlvm(Type.Reg, regId);
                    sdivLlvm.addOperand(reg);
                    reg.addUse(new Use(reg, sdivLlvm, sdivLlvm.getPos()));
                    sdivLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, sdivLlvm, sdivLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(sdivLlvm);
                    reg = sdivLlvm;
                }
            }
        }
        if (reg == null) {
            return new Value(Type.MyConst, value);
        }
        else if (value != 1) {
            int id = IRModule.getRegID();
            MulLlvm mulLlvm = new MulLlvm(Type.Reg, id);
            Value con = new Value(Type.MyConst, value);
            mulLlvm.addOperand(reg);
            reg.addUse(new Use(reg, mulLlvm, mulLlvm.getPos()));
            mulLlvm.addOperand(con);
            IRModule.curFunction.getCurrentBlock().addInstruction(mulLlvm);
            return mulLlvm;
        }
        return reg;
    }


    public int getValue() {
        int value = unaryExps.get(0).getValue(1);
        for (int i = 1; i < unaryExps.size(); i++) {
            if (op.get(i - 1).equals("*")) {
                value *= unaryExps.get(i).getValue(1);
            }
            else if (op.get(i - 1).equals("/")) {
                value /= unaryExps.get(i).getValue(1);
            }
            else {
                value %= unaryExps.get(i).getValue(1);
            }
        }
        return value;
    }
}
