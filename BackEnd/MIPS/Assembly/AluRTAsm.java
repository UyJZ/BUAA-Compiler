package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class AluRTAsm extends RTAsm{

    private final Op op;

    public enum Op {
        add, sub, and, or, xor, slt, sgt, sge, seq, sne
    }
    public AluRTAsm(Op op, Register rd, Register rs, Register rt) {
        super(rd, rs, rt);
        this.op = op;
    }

    @Override
    public String toString() {
        return "\t" + op + "\t" + rd + ", " + rs + ", " + rt;
    }
}
