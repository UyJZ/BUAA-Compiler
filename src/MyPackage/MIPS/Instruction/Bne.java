package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;

public class Bne implements Mips {
    String label;
    String op1;
    String op2;

    public Bne(String op1, String op2, String label) {
        this.op1 = op1;
        this.op2 = op2;
        this.label = label;
    }

    @Override
    public void print() {

    }
}
