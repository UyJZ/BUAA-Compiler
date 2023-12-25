package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.IR.Value;
import MyPackage.MIPS.Instruction.Beq;
import MyPackage.MIPS.Instruction.J;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class BrLlvm extends User {
    private String label1;
    private String label2;
    public BrLlvm(Type type, int value, String label1, String label2) {
        super(type, value);
        this.label1 = label1;
        this.label2 = label2;
    }

    public BrLlvm(String label1) {
        super(Type.Void, 0);
        this.label1 = label1;
        this.label2 = null;
    }



    @Override
    public void print() {
        if (label2 == null) {
            OutPut.printLlvm(String.format("    br label %%%s\n", label1));
        }
        else {
            OutPut.printLlvm(String.format("    br %s %s, label %%%s, label %%%s\n",
                    getOperands().get(0).printType(),getOperands().get(0).printValue(), label1, label2));
        }
    }

    public void generateMips() {
        if (label2 == null) {
            MipsModule.addText(new J(label1));
        }
        else {
            MipsModule.addText(new Beq(MipsModule.searchReg(getOperands().get(0).getValue()), "$zero", label2));
            MipsModule.addText(new J(label1));
        }
    }
}
