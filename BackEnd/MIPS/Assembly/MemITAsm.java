package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class MemITAsm extends ITAsm {

    private Op op;

    // lw and sw
    public enum Op {
        lw, sw, lb, sb, lh, sh
    }

    private Register rs, rt;

    private int offset;

    public MemITAsm(Op op, Register rt, Register rs, int imm) {
        super(rt, rs, imm);
        this.offset = imm;
        this.op = op;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "\t" + op + "\t" + rt + ", " + offset + "(" + rs + ")";
    }
}
