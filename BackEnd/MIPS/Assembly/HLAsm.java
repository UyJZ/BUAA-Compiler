package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class HLAsm extends Asm {

    public enum Op {
        mfhi, mflo, mthi, mtlo
    }

    private Op op;

    public HLAsm(Op op, Register rs) {
        this.op = op;
        this.rs = rs;
    }

    private Register rs;

    @Override
    public String toString() {
        return "\t" + op + "\t" + rs;
    }
}
