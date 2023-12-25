package MyPackage.IR.Instruction;

import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.OutPut;

public class TruncTo extends User {
    public TruncTo(Type type, int value) {
        super(type, value);
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = trunc %s %s to %s\n", super.printValue(),
                getOperands().get(0).printType(), getOperands().get(0).printValue(), super.printType()));
    }


    public void generateMips() {

    }
}
