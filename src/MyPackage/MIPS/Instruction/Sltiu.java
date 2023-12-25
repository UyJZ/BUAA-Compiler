package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Sltiu implements Mips {
    String op1;
    String op2;
    int immediate;

    public Sltiu(String op1, String op2, int immediate) {
        this.op1 = op1;
        this.op2 = op2;
        this.immediate = immediate;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    sltiu %s, %s, %d", op1, op2, immediate));
    }
}
