package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class MoveAsm extends Asm{
    private Register rs, rt;//rt = rs

    public MoveAsm(Register rt, Register rs) {
        this.rs = rs;
        this.rt = rt;
    }
}
