package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.OutPut;

public class OrLlvm extends User {
    public OrLlvm(Type type, int value) {
        super(type, value);
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = or %s %s, %s\n", super.printValue(), super.printType(),
                getOperands().get(0).printValue(), getOperands().get(1).printValue()));
    }


    public void generateMips() {

    }
}
