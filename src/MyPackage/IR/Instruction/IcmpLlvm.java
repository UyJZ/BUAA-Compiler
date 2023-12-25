package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Slt;
import MyPackage.MIPS.Instruction.Sltiu;
import MyPackage.MIPS.Instruction.Xor;
import MyPackage.MIPS.Instruction.Xori;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class IcmpLlvm extends User {
    String cond;
    public IcmpLlvm(Type type, int value, String cond) {
        super(type, value);
        this.cond = cond;
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = icmp %s %s %s, %s\n", super.printValue(), cond, getOperands().get(0).printType(),
                getOperands().get(0).printValue(), getOperands().get(1).printValue()));
    }


    public void generateMips() {
        if (cond.equals("eq")) {
            String op1 = MipsModule.getReg();
            String op2;
            String op3;
            if (getOperands().get(0).getType().equals(Type.MyConst) &&
                    getOperands().get(1).getType().equals(Type.MyConst)) {
                if (getOperands().get(0).getValue() == getOperands().get(1).getValue()) {
                    MipsModule.addText(new Li(op1, 1));
                }
                else {
                    MipsModule.addText(new Li(op1, 0));
                }
                MipsModule.addReg(getValue(), op1);
                return;
            }
            else if (getOperands().get(0).getType().equals(Type.MyConst)) {
                op2 = MipsModule.searchReg(getOperands().get(1).getValue());
                op3 = "$at";
                MipsModule.addText(new Li("$at", getOperands().get(0).getValue()));
            }
            else if (getOperands().get(1).getType().equals(Type.MyConst)) {
                op2 = MipsModule.searchReg(getOperands().get(0).getValue());
                op3 = "$at";
                MipsModule.addText(new Li("$at", getOperands().get(1).getValue()));
            }
            else {
                op2 = MipsModule.searchReg(getOperands().get(0).getValue());
                op3 = MipsModule.searchReg(getOperands().get(1).getValue());
            }
            MipsModule.addText(new Xor(op1, op2, op3));
            MipsModule.addText(new Sltiu(op1, op1, 1));
            MipsModule.addReg(getValue(), op1);
        }
        else if(cond.equals("ne")) {
            String op1 = MipsModule.getReg();
            String op2;
            String op3;
            if (getOperands().get(0).getType().equals(Type.MyConst) &&
                    getOperands().get(1).getType().equals(Type.MyConst)) {
                if (getOperands().get(0).getValue() != getOperands().get(1).getValue()) {
                    MipsModule.addText(new Li(op1, 1));
                }
                else {
                    MipsModule.addText(new Li(op1, 0));
                }
                MipsModule.addReg(getValue(), op1);
                return;
            }
            else if (getOperands().get(0).getType().equals(Type.MyConst)) {
                op2 = MipsModule.searchReg(getOperands().get(1).getValue());
                op3 = "$at";
                MipsModule.addText(new Li("$at", getOperands().get(0).getValue()));
            }
            else if (getOperands().get(1).getType().equals(Type.MyConst)) {
                op2 = MipsModule.searchReg(getOperands().get(0).getValue());
                op3 = "$at";
                MipsModule.addText(new Li("$at", getOperands().get(1).getValue()));
            }
            else {
                op2 = MipsModule.searchReg(getOperands().get(0).getValue());
                op3 = MipsModule.searchReg(getOperands().get(1).getValue());
            }
            MipsModule.addText(new Xor(op1, op2, op3));
            MipsModule.addText(new Sltiu(op1, op1, 1));
            MipsModule.addText(new Xori(op1, op1, 1));
            MipsModule.addReg(getValue(), op1);
        }
        else {
            new Slt(getValue(), getOperands().get(0), getOperands().get(1), cond);
        }
    }
}
