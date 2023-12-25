package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Lw implements Mips {
    int off;

    String offName;
    String op1;
    String op2;

    public Lw(String op1, String op2, int off) {
        this.off = off;
        this.op1 = op1;
        this.op2 = op2;
        offName = null;
    }

    public Lw(String op1, String op2, String offName) {
        this.off = 0;
        this.op1 = op1;
        this.op2 = op2;
        this.offName = offName;
    }

    @Override
    public void print() {
        if (offName == null) {
            OutPut.printMips(String.format("    lw %s, %d(%s)", op1, off, op2));
        }
        else {
            OutPut.printMips(String.format("    lw %s, %s(%s)", op1, offName, op2));
        }
    }
}
