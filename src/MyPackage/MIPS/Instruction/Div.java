package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Div implements Mips {
    String op1;
    String op2;

    public Div(String op1, String op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    div %s, %s", op1, op2));
    }
}
