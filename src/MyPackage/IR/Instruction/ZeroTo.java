package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

public class ZeroTo extends User {
    public ZeroTo(Type type, int value) {
        super(type, value);
    }

    public void print() {
        OutPut.printLlvm(String.format("    %s = zext %s %s to %s\n", super.printValue(),
                getOperands().get(0).printType(), getOperands().get(0).printValue(), super.printType()));
    }


    public void generateMips() {
        String op = MipsModule.searchReg2(getOperands().get(0).getValue());
        if (op == null) {
            int off = MipsModule.getAddress(getOperands().get(0).getValue());
            MipsModule.addAddress(getValue(), off);
        }
        else {
            MipsModule.addReg(getValue(), op);
        }
    }
}
