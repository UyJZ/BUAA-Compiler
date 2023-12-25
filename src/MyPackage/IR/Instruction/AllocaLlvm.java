package MyPackage.IR.Instruction;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.Instruction.Sw;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class AllocaLlvm extends User{
    private ArrayLlvm arrayLlvm;
    public AllocaLlvm(Type type, int value) {
        super(type, value);
        if (type.equals(Type.Array)) {
            arrayLlvm = new ArrayLlvm(type, value);
        }
        else {
            arrayLlvm = null;
        }
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = alloca %s\n", super.printValue() , printType()));
    }

    @Override
    public String printType() {
        if (arrayLlvm == null) {
            return super.printType();
        }
        else {
            return arrayLlvm.printType();
        }
    }

    public ArrayLlvm getArrayLlvm() {
        return arrayLlvm;
    }

    public void setArrayLlvm(ArrayLlvm arrayLlvm) {
        this.arrayLlvm = arrayLlvm;
    }

    public void generateMips() {
        MipsModule.addAlloc(getValue());
        if (arrayLlvm == null) {
            int off = MipsModule.getStack();
            MipsModule.addStack(4);
            MipsModule.addAddress(getValue(), off);
        }
        else {
            MipsModule.addStack(arrayLlvm.getSize());
            MipsModule.addAddress(getValue(), MipsModule.getStack() + 4);
            MipsModule.clearTimes = arrayLlvm.getSize() - 4;
        }
    }
}