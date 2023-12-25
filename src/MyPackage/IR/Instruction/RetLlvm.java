package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Add;
import MyPackage.MIPS.Instruction.Jr;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Syscall;
import MyPackage.MIPS.Mips;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;


public class RetLlvm extends User {
    public RetLlvm(Type type, int value) {
        super(type, value);
    }

    @Override
    public void print() {
        if (getType().equals(Type.Void)) {
            OutPut.printLlvm("    ret void\n");
        }
        else {
            OutPut.printLlvm(String.format("    ret %s %s\n", super.printType(), getOperands().get(0).printValue()));
        }
    }

    public void generateMips() {
        if (MipsModule.isMain) {
            MipsModule.addText(new Li("$v0", 10));
            MipsModule.addText(new Syscall());
            return;
        }
        if (getType().equals(Type.Void)) {
            MipsModule.addText(new Jr("$ra"));
        }
        else {
            String op;
            if (getOperands().get(0).getType().equals(Type.MyConst)) {
                op = MipsModule.getReg();
                MipsModule.addText(new Li(op, getOperands().get(0).getValue()));
            }
            else {
                op = MipsModule.searchReg(getOperands().get(0).getValue());
            }
            MipsModule.addText(new Add("$v0", op, "$0"));
            MipsModule.addText(new Jr("$ra"));
        }
    }
}
