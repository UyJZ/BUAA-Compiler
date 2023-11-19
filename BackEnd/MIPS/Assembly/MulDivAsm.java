package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class MulDivAsm extends Asm {
    private Register rd, rt;

    public enum Op {
        mult, multu, div, divu
    }
}
