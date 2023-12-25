package MyPackage.Parse;


import MyPackage.IR.Instruction.CallLlvm;
import MyPackage.IR.Instruction.IcmpLlvm;
import MyPackage.IR.Instruction.SubLlvm;
import MyPackage.IR.Instruction.ZeroTo;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.OutPut;
import MyPackage.Parser;
import MyPackage.Symbol.FunSymbol;
import MyPackage.Symbol.Symbol;
import MyPackage.IR.IRModule;

import java.util.ArrayList;

public class UnaryExp {
    private int type;
    private PrimaryExp primaryExp;
    private String ident;
    private FuncRParams funcRParams;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(PrimaryExp primaryExp) {
        type = 0;
        this.primaryExp = primaryExp;
    }

    public UnaryExp(String ident, FuncRParams funcRParams) {
        type = 1;
        this.ident = ident;
        this.funcRParams = funcRParams;
    }

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        type = 2;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public int getLevel() {
        if (type == 1) {
            Symbol symbol = Parser.getRoot().search(ident);
            if (symbol instanceof FunSymbol) {
                if (symbol.getType().equals("void")) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        }
        if (type != 0) {
            return 0;
        }
        return primaryExp.getLevel();
    }

    public Value generateLlvm(int op) {
        Value pair;
        if (type == 0) {
            pair = primaryExp.generateLlvm();
            if (op == -1 && !pair.getType().equals(Type.MyConst)) {
                int regId = IRModule.getRegID();
                SubLlvm subLlvm = new SubLlvm(Type.Reg, regId);
                Value value = new Value(Type.MyConst, 0);
                subLlvm.addOperand(value);
                subLlvm.addOperand(pair);
                pair.addUse(new Use(pair, subLlvm, subLlvm.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(subLlvm);
                pair = subLlvm;
            }
            else if (op == -1){
                pair.setValue(-pair.getValue());
            }
        }
        else if (type == 1) {
            FunSymbol funSymbol = (FunSymbol) IRModule.getRoot().search(ident);
            Type type1;
            int id;
            ArrayList<Value> values = new ArrayList<>();
            if (funcRParams != null) {
                funcRParams.generateLlvm(values);
            }
            if (funSymbol.getType().equals("void")) {
                type1 = Type.Void;
                id = 0;
            }
            else {
                type1 = Type.Reg;
                id = IRModule.getRegID();
            }
            CallLlvm callLlvm = new CallLlvm(type1, id, ident);
            for (int i = 0; i < values.size(); i++) {
                values.get(i).addUse(new Use(values.get(i), callLlvm, i));
            }
            callLlvm.setOperands(values);
            IRModule.curFunction.getCurrentBlock().addInstruction(callLlvm);
            if (op == -1) {
                int regId = IRModule.getRegID();
                SubLlvm subLlvm = new SubLlvm(Type.Reg, regId);
                Value value = new Value(Type.MyConst, 0);
                subLlvm.addOperand(value);
                subLlvm.addOperand(callLlvm);
                callLlvm.addUse(new Use(callLlvm, subLlvm, subLlvm.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(subLlvm);
                return subLlvm;
            }
            else {
                return callLlvm;
            }
        }
        else {
            if (unaryOp.getOp().equals("+")) {
                pair = unaryExp.generateLlvm(op);
            }
            else if (unaryOp.getOp().equals("-")) {
                pair = unaryExp.generateLlvm(-op);
            }
            else {
                pair = unaryExp.generateLlvm(op);
                int id = IRModule.getRegID();
                IcmpLlvm icmpLlvm = new IcmpLlvm(Type.Boolean, id, "eq");
                icmpLlvm.addOperand(new Value(Type.MyConst, 0));
                icmpLlvm.addOperand(pair);
                pair.addUse(new Use(pair, icmpLlvm, icmpLlvm.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(icmpLlvm);
                id = IRModule.getRegID();
                ZeroTo zeroTo = new ZeroTo(Type.Reg, id);
                zeroTo.addOperand(icmpLlvm);
                icmpLlvm.addUse(new Use(icmpLlvm, zeroTo, zeroTo.getPos()));
                pair = zeroTo;
                IRModule.curFunction.getCurrentBlock().addInstruction(zeroTo);
                if (op == -1) {
                    int regId = IRModule.getRegID();
                    SubLlvm subLlvm = new SubLlvm(Type.Reg, regId);
                    Value value = new Value(Type.MyConst, 0);
                    subLlvm.addOperand(value);
                    subLlvm.addOperand(pair);
                    pair.addUse(new Use(pair, subLlvm, subLlvm.getPos()));
                    IRModule.curFunction.getCurrentBlock().addInstruction(subLlvm);
                    pair = subLlvm;
                }
            }
        }
        return pair;
    }


    public int getValue(int op) {
        int value;
        if (type == 0) {
            value = primaryExp.getValue();
            if (op == -1) {
                value = -value;
            }
        }
        else {
            if (unaryOp.getOp().equals("-")) {
                value = unaryExp.getValue(-op);
            }
            else {
                value = unaryExp.getValue(op);
            }
        }
        return value;
    }
}
