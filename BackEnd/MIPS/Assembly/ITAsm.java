package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class ITAsm extends Asm{

    protected Register rt, rs; // rd = rs op rt

    protected int imm;

    public ITAsm(Register rt, Register rs, int imm) {
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
    }

}
