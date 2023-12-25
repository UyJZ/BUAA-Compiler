package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class La implements Mips {
    String op1;
    String immediate;

    public La(String op1, String immediate) {
        this.op1 = op1;
        this.immediate = immediate;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    la %s, %s", op1, immediate));
    }
}
