package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class LiAsm extends Asm {
    private int imm;

    private Register rs, rt;

    public LiAsm(Register rt,Register rs, int imm) {
        this.rt = rt;
        this.rs = rs;
        this.imm = imm;
    }
}
