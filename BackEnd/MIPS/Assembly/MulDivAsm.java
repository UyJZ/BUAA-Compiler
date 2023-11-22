package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class MulDivAsm extends Asm {
    private Register rs, rt;

    private Op opcode;

    public MulDivAsm(Op op, Register rs, Register rt) {
        this.rs = rs;
        this.rt = rt;
        opcode = op;
    }

    public enum Op {
        mult, multu, div, divu
    }

    @Override
    public String toString() {
        return "\t" + opcode + "\t" + rs + ", " + rt;
    }
}
