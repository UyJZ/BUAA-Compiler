package MyPackage.MIPS.Instruction;

import MyPackage.MIPS.Mips;
import MyPackage.OutPut;

public class Syscall implements Mips {
    public Syscall() {

    }

    @Override
    public void print() {
        OutPut.printMips("    syscall");
    }
}
