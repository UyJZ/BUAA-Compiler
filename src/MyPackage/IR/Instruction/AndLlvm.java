package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Add;
import MyPackage.MIPS.Instruction.Addi;
import MyPackage.MIPS.Instruction.And;
import MyPackage.MIPS.Instruction.Andi;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class AndLlvm extends User {
    public AndLlvm(Type type, int value) {
        super(type, value);
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = and %s %s, %s\n", super.printValue(), super.printType(),
                getOperands().get(0).printValue(), getOperands().get(1).printValue()));
    }

    public void generateMips() {
        String op1;
        String op2;
        String op3;
        int immediate;
        if (getOperands().get(0).getType().equals(Type.MyConst)) {
            immediate = getOperands().get(0).getValue();
            int reg = getOperands().get(1).getValue();
            op2 = MipsModule.searchReg(reg);
            op1 = MipsModule.getReg();
            MipsModule.addText(new Andi(op1, op2, immediate));
            MipsModule.addReg(getValue(), op1);
        }
        else if (getOperands().get(1).getType().equals(Type.MyConst)) {
            immediate = getOperands().get(1).getValue();
            int reg = getOperands().get(0).getValue();
            op2 = MipsModule.searchReg(reg);
            op1 = MipsModule.getReg();
            MipsModule.addText(new Andi(op1, op2, immediate));
            MipsModule.addReg(getValue(), op1);
        }
        else {
            op2 = MipsModule.searchReg(getOperands().get(0).getValue());
            op3 = MipsModule.searchReg(getOperands().get(1).getValue());
            op1 = MipsModule.getReg();
            MipsModule.addText(new And(op1, op2, op3));
            MipsModule.addReg(getValue(), op1);
        }
    }
}

