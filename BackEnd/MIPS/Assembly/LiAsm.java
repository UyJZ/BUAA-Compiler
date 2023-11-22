package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class LiAsm extends Asm {
    private int imm;

    private Register rt;

    public LiAsm(Register rt, int imm) {
        this.rt = rt;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return "\tli\t" + rt + ", " + imm;
    }
}
