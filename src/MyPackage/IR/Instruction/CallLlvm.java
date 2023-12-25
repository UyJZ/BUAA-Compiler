package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.IR.Value;
import MyPackage.MIPS.Instruction.Add;
import MyPackage.MIPS.Instruction.Addi;
import MyPackage.MIPS.Instruction.Jal;
import MyPackage.MIPS.Instruction.La;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Lw;
import MyPackage.MIPS.Instruction.Sw;
import MyPackage.MIPS.Instruction.Syscall;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class CallLlvm extends User {
    String funSymbol;
    public CallLlvm(Type type, int value, String funSymbol) {
        super(type, value);
        this.funSymbol = funSymbol;
    }

    @Override
    public void print() {
        if (!getType().equals(Type.Void)) {
            OutPut.printLlvm(String.format("    %s = ", super.printValue()));
        }
        else {
            OutPut.printLlvm("    ");
        }
        OutPut.printLlvm(String.format("call %s @%s(", super.printType(), funSymbol));
        for (int i = 0; i < getOperands().size(); i++) {
            Value value = getOperands().get(i);
            if (i != getOperands().size() - 1) {
                OutPut.printLlvm(String.format("%s %s, ", value.printType(), value.printValue()));
            }
            else {
                OutPut.printLlvm(String.format("%s %s)\n", value.printType(), value.printValue()));
            }
        }
        if (getOperands().size() == 0) {
            OutPut.printLlvm(")\n");
        }
    }


    public void generateMips() {
        if (funSymbol.equals("getint")) {
            MipsModule.addText(new Addi("$v0", "$0", 5));
            MipsModule.addText(new Syscall());
        }
        else if (funSymbol.equals("putch")) {
            if (MipsModule.stringBuilder == null) {
                MipsModule.stringBuilder = new StringBuilder();
                MipsModule.stringBuilder.append((char) getOperands().get(0).getValue());
                MipsModule.addText(new Addi("$v0", "$0", 4));
                MipsModule.addText(new La("$a0", String.format("lhc_str%d", MipsModule.getStringCount())));
                MipsModule.addText(new Syscall());
                MipsModule.isPut = true;
            }
            else {
                MipsModule.stringBuilder.append((char) getOperands().get(0).getValue());
                MipsModule.isPut = true;
            }
        }
        else if (funSymbol.equals("putint")) {
            Value value = getOperands().get(0);
            MipsModule.addText(new Addi("$v0", "$0", 1));
            if (value.getType().equals(Type.MyConst)) {
                MipsModule.addText(new Li("$a0", value.getValue()));
            }
            else {
                String op = MipsModule.searchReg(value.getValue());
                MipsModule.addText(new Add("$a0", op, "$0"));
            }
            MipsModule.addText(new Syscall());
        }
        else {
            MipsModule.saveReg();
            int off = MipsModule.getStack();
            MipsModule.addText(new Sw("$ra", "$sp", off));
            for (int i = 0; i < getOperands().size(); i++) {
                Value value = getOperands().get(i);
                String op;
                if (value.getType() == Type.MyConst) {
                    op = MipsModule.getReg();
                    MipsModule.addText(new Li(op, value.getValue()));
                }
                else {
                    op = MipsModule.searchReg(value.getValue());
                }
                if (i < 4) {
                    MipsModule.addText(new Add(String.format("$a%d", i), op, "$0"));
                }
                else {
                    MipsModule.addText(new Sw(op, "$sp", off - 4 * (i - 3)));
                }
            }
            MipsModule.addText(new Addi("$sp", "$sp", off));
            MipsModule.regClear();
            MipsModule.addText(new Jal(funSymbol));
            MipsModule.addText(new Lw("$ra", "$sp", 0));
            MipsModule.addText(new Addi("$sp", "$sp", -off));
        }
        if (!getType().equals(Type.Void)) {
            String op = MipsModule.getReg();
            MipsModule.addText(new Add(op, "$v0", "$0"));
            MipsModule.addReg(getValue(), op);
        }
    }

}
