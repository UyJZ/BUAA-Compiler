package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Mflo implements Mips {
    String op;

    public Mflo(String op) {
        this.op = op;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    mflo %s", op));
    }
}
