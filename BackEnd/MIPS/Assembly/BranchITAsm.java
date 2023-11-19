package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class BranchITAsm extends ITAsm {
    //beq, bne, blez, bgtz, bltz, bgez
    public enum Op {
        beq, bne
    }

    private Op op;

    private LabelAsm label;

    public BranchITAsm(Op op, Register rs, Register rt, LabelAsm label) {
        super(rs, rt, 0);
        this.op = op;
        this.label = label;
    }
}
