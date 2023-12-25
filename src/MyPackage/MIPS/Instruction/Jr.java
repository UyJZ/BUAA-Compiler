package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Jr implements Mips {
    String op;
    public Jr(String op) {
        this.op = op;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    jr %s", op));
    }
}
