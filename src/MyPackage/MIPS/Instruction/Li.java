package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Li implements Mips {
    String op1;
    int immediate;

    public Li(String op1, int immediate) {
        this.op1 = op1;
        this.immediate = immediate;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    li %s, %d", op1, immediate));
    }
}
