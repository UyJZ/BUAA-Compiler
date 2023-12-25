package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Lw;
import MyPackage.MIPS.Instruction.Sw;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;
import MyPackage.Symbol.MyValSymbol;

public class StoreLlvm extends User {
    private MyValSymbol symbol;
    public StoreLlvm(Type type, int value, MyValSymbol symbol) {
        super(type, value);
        this.symbol = symbol;
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    store %s %s, %s * ", getOperands().get(0).printType(), getOperands().get(0).printValue(),
                    getOperands().get(0).printType()));
        if (symbol.isGlobal() && getOperands().get(1) == symbol.getReg()) {
            OutPut.printLlvm(String.format("@%s\n", symbol.getName()));
        }
        else {
            OutPut.printLlvm(String.format("%s\n", getOperands().get(1).printValue()));
        }
    }


    public void generateMips() {
        String op1;
        if (getOperands().get(0).getType().equals(Type.MyConst)) {
            op1 = MipsModule.getReg();
            MipsModule.addText(new Li(op1, getOperands().get(0).getValue()));
        }
        else {
            op1 = MipsModule.searchReg(getOperands().get(0).getValue());
        }
        if (symbol.isGlobal() && getOperands().get(1) == symbol.getReg()) {
            MipsModule.addText(new Sw(op1, "$0", symbol.getName()));
        }
        else {
            if (MipsModule.alloc.containsKey(getOperands().get(1).getValue())) {
                int off = MipsModule.getAddress(getOperands().get(1).getValue());
                MipsModule.addText(new Sw(op1, "$sp", off));
            }
            else {
                String op = MipsModule.searchReg(getOperands().get(1).getValue());
                MipsModule.addText(new Sw(op1, op, 0));
            }
        }
        if (MipsModule.clearTimes == 0) {
            MipsModule.regClear();
        }
        else {
            MipsModule.clearTimes = MipsModule.clearTimes - 4;
        }
    }
}
