package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Jal implements Mips {
    String label;

    public Jal(String label) {
        this.label = label;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    jal %s", label));
    }
}
