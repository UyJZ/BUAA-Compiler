package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class RTAsm extends Asm {

    protected Register rd, rs, rt; // rd = rs op rt

    public RTAsm(Register rd, Register rs, Register rt) {
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public Register getRd() {
        return rd;
    }

    public Register getRs() {
        return rs;
    }

    public Register getRt() {
        return rt;
    }

}
