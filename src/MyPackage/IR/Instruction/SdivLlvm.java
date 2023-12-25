package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Div;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Mflo;
import MyPackage.MIPS.Instruction.Mult;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class SdivLlvm extends User {
    public SdivLlvm(Type type, int value) {
        super(type, value);
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = sdiv %s %s, %s\n", super.printValue(), super.printType(),
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
            op3 = MipsModule.getReg();
            MipsModule.addText(new Li(op3, immediate));
            MipsModule.addText(new Div(op2, op3));
            op1 = MipsModule.getReg();
            MipsModule.addText(new Mflo(op1));
            MipsModule.addReg(getValue(), op1);
        }
        else if (getOperands().get(1).getType().equals(Type.MyConst)) {
            immediate = getOperands().get(1).getValue();
            int reg = getOperands().get(0).getValue();
            op2 = MipsModule.searchReg(reg);
            op3 = MipsModule.getReg();
            MipsModule.addText(new Li(op3, immediate));
            MipsModule.addText(new Div(op2, op3));
            op1 = MipsModule.getReg();
            MipsModule.addText(new Mflo(op1));
            MipsModule.addReg(getValue(), op1);
        }
        else {
            op2 = MipsModule.searchReg(getOperands().get(0).getValue());
            op3 = MipsModule.searchReg(getOperands().get(1).getValue());
            MipsModule.addText(new Div(op2, op3));
            op1 = MipsModule.getReg();
            MipsModule.addText(new Mflo(op1));
            MipsModule.addReg(getValue(), op1);
        }
    }
}
