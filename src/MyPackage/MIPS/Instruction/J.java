package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class J implements Mips {
    String string;

    public J(String string) {
        this.string = string;
    }

    @Override
    public void print() {
        OutPut.printMips(String.format("    j %s", string));
    }
}
