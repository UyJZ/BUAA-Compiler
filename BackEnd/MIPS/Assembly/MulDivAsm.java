package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class MulDivAsm extends Asm {
    private Register rs, rt;

    public MulDivAsm(Register rs, Register rt) {
        this.rs = rs;
        this.rt = rt;
    }

    public enum Op {
        mult, multu, div, divu
    }
}
