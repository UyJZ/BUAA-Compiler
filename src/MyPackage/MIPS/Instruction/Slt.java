package MyPackage.MIPS.Instruction;

import MyPackage.IR.IRModule;
import MyPackage.IR.Type;
import MyPackage.IR.Value;
import MyPackage.MIPS.Mips;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class Slt implements Mips {
    String op1;
    String op2;
    String op3 = null;
    String type;
    int imm = 0;

    public Slt(String op1, String op2, String op3) {
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
    }

    public Slt(int value1, Value value2, Value value3, String type) {
        op1 = MipsModule.getReg();
        if (value3.getType().equals(Type.MyConst) && value2.getType().equals(Type.MyConst)) {
            switch (type) {
                case "slt":
                    MipsModule.addText(new Li(op1, value2.getValue() < value3.getValue() ? 1 : 0));
                    break;
                case "sle":
                    MipsModule.addText(new Li(op1, value2.getValue() <= value3.getValue() ? 1 : 0));
                    break;
                case "sgt":
                    MipsModule.addText(new Li(op1, value2.getValue() > value3.getValue() ? 1 : 0));
                    break;
                case "sge":
                    MipsModule.addText(new Li(op1, value2.getValue() >= value3.getValue() ? 1 : 0));
                    break;
            }
        }
        else if (value3.getType().equals(Type.MyConst)) {
            op2 = MipsModule.searchReg(value2.getValue());
            imm = value3.getValue();
            op3 = null;
            if (type.equals("slt")) {
                if (imm < -32768 || imm > 32767) {
                    MipsModule.addText(new Li("$at", imm));
                    op3 = "$at";
                    this.type = "slt";
                }
                else {
                    this.type = "slti";
                }
            }
            else {
                this.type = type;
            }
            MipsModule.addText(this);
        }
        else if (value2.getType().equals(Type.MyConst)) {
            op2 = MipsModule.searchReg(value3.getValue());
            imm = value2.getValue();
            op3 = null;
            switch (type) {
                case "eq":
                case "ne":
                    this.type = type;
                    break;
                case "slt":
                    this.type = "sge";
                    break;
                case "sle":
                    this.type = "sgt";
                    break;
                case "sgt":
                    this.type = "sle";
                    break;
                case "sge":
                    if (imm < -32768 || imm > 32767) {
                        MipsModule.addText(new Li("$at", imm));
                        op3 = "$at";
                        this.type = "slt";
                    }
                    else {
                        this.type = "slti";
                    }
                    break;
            }
            MipsModule.addText(this);
        }
        else {
            op2 = MipsModule.searchReg(value2.getValue());
            op3 = MipsModule.searchReg(value3.getValue());
            this.type = type;
            MipsModule.addText(this);
        }
        MipsModule.addReg(value1, op1);
    }

    @Override
    public void print() {
        if (op3 == null) {
            OutPut.printMips(String.format("    %s %s, %s, %d", type, op1, op2, imm));
        }
        else {
            OutPut.printMips(String.format("    %s %s, %s, %s", type, op1, op2, op3));
        }
    }
}
