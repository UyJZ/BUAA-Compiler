package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.IcmpLlvm;
import MyPackage.IR.Instruction.ZeroTo;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class EqExp {
    private ArrayList<RelExp> relExps;
    private ArrayList<String> op;
    public EqExp(ArrayList<RelExp> relExps, ArrayList<String> op) {
        this.relExps = relExps;
        this.op = op;
    }

    public Value generateLlvm() {
        Value reg = relExps.get(0).generateLlvm();
        for (int i = 1; i < relExps.size(); i++) {
            if (reg.getType().equals(Type.Boolean)) {
                int id = IRModule.getRegID();
                ZeroTo zeroTo = new ZeroTo(Type.Reg, id);
                zeroTo.addOperand(reg);
                reg.addUse(new Use(reg, zeroTo, zeroTo.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(zeroTo);
                reg = zeroTo;
            }
            Value value = relExps.get(i).generateLlvm();
            if (value.getType().equals(Type.Boolean)) {
                int id = IRModule.getRegID();
                ZeroTo zeroTo = new ZeroTo(Type.Reg, id);
                zeroTo.addOperand(value);
                value.addUse(new Use(value, zeroTo, zeroTo.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(zeroTo);
                value = zeroTo;
            }
            int id = IRModule.getRegID();
            IcmpLlvm icmpLlvm;
            if (op.get(i-1).equals("==")) {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "eq");
            }
            else {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "ne");
            }
            icmpLlvm.addOperand(reg);
            reg.addUse(new Use(reg, icmpLlvm, icmpLlvm.getPos()));
            icmpLlvm.addOperand(value);
            value.addUse(new Use(value, icmpLlvm, icmpLlvm.getPos()));
            IRModule.curFunction.getCurrentBlock().addInstruction(icmpLlvm);
            reg = icmpLlvm;
        }
        return reg;
    }
}
