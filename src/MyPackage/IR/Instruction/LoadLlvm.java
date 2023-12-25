package MyPackage.IR.Instruction;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.IRModule;
import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Lw;
import MyPackage.MIPS.Mips;
import MyPackage.MIPS.MipsDecl;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;

public class LoadLlvm extends User {
    MyValSymbol symbol;
    ArrayLlvm arrayLlvm;
    public LoadLlvm(Type type, int value, Symbol symbol) {
        super(type, value);
        this.symbol = (MyValSymbol) symbol;
    }


    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = load %s, %s * ", super.printValue(), getOperands().get(1).printType(),
                    getOperands().get(1).printType()));
         if (symbol.isGlobal() && getOperands().get(1) == symbol.getReg()) {
            OutPut.printLlvm(String.format("@%s\n", symbol.getName()));
            }
        else {
            OutPut.printLlvm(String.format("%s\n", getOperands().get(1).printValue()));
        }
    }

    public void setArrayLlvm(ArrayLlvm arrayLlvm) {
        this.arrayLlvm = arrayLlvm;
    }

    @Override
    public String printType() {
        if (getType().equals(Type.Array)) {
            return arrayLlvm.printType();
        }
        else {
            return super.printType();
        }
    }

    public ArrayLlvm getArrayLlvm() {
        return arrayLlvm;
    }

    public void generateMips() {
        String op1 = MipsModule.getReg();
        if (symbol.isGlobal() && getOperands().get(1) == symbol.getReg()) {
            MipsModule.addText(new Lw(op1, "$0", symbol.getName()));
        }
        else {
            if (MipsModule.alloc.containsKey(getOperands().get(1).getValue())) {
                int off = MipsModule.getAddress(getOperands().get(1).getValue());
                MipsModule.addText(new Lw(op1, "$sp", off));
            }
            else {
                String op = MipsModule.searchReg(getOperands().get(1).getValue());
                MipsModule.addText(new Lw(op1, op, 0));
            }
        }
        MipsModule.addReg(getValue(), op1);
    }
}
