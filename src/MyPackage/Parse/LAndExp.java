package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.BrLlvm;
import MyPackage.IR.Instruction.IcmpLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class LAndExp {
    private ArrayList<EqExp> eqExps;

    public LAndExp (ArrayList<EqExp> eqExps) {
        this.eqExps = eqExps;
    }

    public void generateLlvm(int index, int max, int id) {
        for (int i = 0; i < eqExps.size(); i++) {
            if (i != 0) {
                IRModule.curFunction.newBlock(String.format("cond_%d_%d_%d",
                        id, index, i));
            }
            Value reg = eqExps.get(i).generateLlvm();
            if (!reg.getType().equals(Type.Boolean)) {
                Value value = new Value(Type.MyConst, 0);
                int regId = IRModule.getRegID();
                IcmpLlvm icmpLlvm = new IcmpLlvm(Type.Boolean, regId, "ne");
                icmpLlvm.addOperand(reg);
                reg.addUse(new Use(reg, icmpLlvm, icmpLlvm.getPos()));
                icmpLlvm.addOperand(value);
                IRModule.curFunction.getCurrentBlock().addInstruction(icmpLlvm);
                reg = icmpLlvm;
            }
            BrLlvm brLlvm;
            if (i == eqExps.size() - 1 && index == max - 1) {
                brLlvm = new BrLlvm(Type.Void, 0,
                        String.format("stmt_%d", id),
                        String.format("end_%d", id));
            }
            else if (index == max - 1) {
                brLlvm = new BrLlvm(Type.Void, 0,
                        String.format("cond_%d_%d_%d", id, index, i+1),
                        String.format("end_%d", id));
            }
            else if (i == eqExps.size() - 1) {
                brLlvm = new BrLlvm(Type.Void, 0,
                        String.format("stmt_%d", id),
                        String.format("cond_%d_%d", id, index+1));
            }
            else {
                brLlvm = new BrLlvm(Type.Void, 0,
                        String.format("cond_%d_%d_%d", id, index, i+1),
                        String.format("cond_%d_%d", id, index+1));
            }
            brLlvm.addOperand(reg);
            IRModule.curFunction.getCurrentBlock().addInstruction(brLlvm);
        }
    }

}
