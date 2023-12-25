package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Mfhi implements Mips {
    String op;

    public Mfhi(String op) {
        this.op = op;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    mfhi %s", op));
    }
}
