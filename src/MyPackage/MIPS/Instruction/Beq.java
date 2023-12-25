package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Beq implements Mips {
    String label;
    String op1;
    String op2;

    public Beq(String op1, String op2, String label) {
        this.op1 = op1;
        this.op2 = op2;
        this.label = label;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    beq %s, %s, %s", op1, op2, label));
    }
}
