package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Andi implements Mips {
    String op1;
    String op2;
    int immediate;

    public Andi(String op1, String op2, int immediate) {
        this.op1 = op1;
        this.op2 = op2;
        this.immediate = immediate;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    andi %s, %s, %s", op1, op2, immediate));
    }
}
