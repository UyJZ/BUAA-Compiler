package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class AluITAsm extends ITAsm {

    public enum Op {
        addi, addiu, slti, sltiu, andi, ori, xori, lui, sll, srl
    }

    private Op op;

    private final Register destination;
    private final Register root;

    public AluITAsm(Op op, Register rd, Register rt, int imm) {
        super(rd, rt, imm);
        this.destination = rd;
        this.root = rt;
        this.op = op;
    }

    @Override
    public String toString() {
        return "\t" + op + "\t" + destination + ", " + root + ", " + imm;
    }
}
