package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class CmpITAsm extends ITAsm{
    // slt, sltu

    public enum Op {
        slti
    }

    public CmpITAsm(Register rt, Register rs, int imm) {
        super(rt, rs, imm);
    }
}
