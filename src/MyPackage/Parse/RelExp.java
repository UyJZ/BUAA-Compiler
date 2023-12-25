package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.IcmpLlvm;
import MyPackage.IR.Instruction.ZeroTo;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class RelExp {
    private ArrayList<AddExp> addExps;
    private ArrayList<String> op;

    public RelExp(ArrayList<AddExp> addExps, ArrayList<String> op) {
        this.addExps = addExps;
        this.op = op;
    }

    public Value generateLlvm() {
        Value reg = addExps.get(0).generateLlvm();
        for (int i = 1; i < addExps.size(); i++) {
            if (reg.getType().equals(Type.Boolean)) {
                int id = IRModule.getRegID();
                ZeroTo zeroTo = new ZeroTo(Type.Reg, id);
                zeroTo.addOperand(reg);
                reg.addUse(new Use(reg, zeroTo, zeroTo.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(zeroTo);
                reg = zeroTo;
            }
            Value value = addExps.get(i).generateLlvm();
            int id = IRModule.getRegID();
            IcmpLlvm icmpLlvm;
            if (op.get(i-1).equals("<")) {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "slt");
            }
            else if (op.get(i-1).equals("<=")) {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "sle");
            }
            else if (op.get(i-1).equals(">")) {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "sgt");
            }
            else {
                icmpLlvm = new IcmpLlvm(Type.Boolean, id, "sge");
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
