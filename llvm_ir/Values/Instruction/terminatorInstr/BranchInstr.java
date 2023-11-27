package llvm_ir.Values.Instruction.terminatorInstr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.ConstBool;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.BoolType;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class BranchInstr extends Instr {

    private final Value judge;

    private final BasicBlock label1, label2;

    public BranchInstr(LLVMType type, BasicBlock label1, BasicBlock label2, Value judge) {
        super(type, "");
        this.label1 = label1;
        this.label2 = label2;
        this.judge = judge;
        this.addValue(judge);
    }

    public BranchInstr(LLVMType type, BasicBlock label1) {
        super(type, "");
        this.label1 = label1;
        this.label2 = null;
        this.judge = null;
    }

    @Override
    public String toString() {
        if (label2 == null && judge == null)
            return "br label " + label1.getName();
        return "br " + new BoolType().toString() + " " + judge.getName() + ", label " + label1.getName() + ", label " + label2.getName();
    }

    @Override
    public void genMIPS() {
        CommentAsm asm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(asm);
        if (label2 == null && judge == null) {
            JAsm j = new JAsm(label1.getMIPSLabel());
            MipsController.getInstance().addAsm(j);
        } else {
            assert (label2 != null && judge != null);
            if (judge.isUseReg()) {
                BranchITAsm bne = new BranchITAsm(BranchITAsm.Op.bne, judge.getRegister(), Register.ZERO, label1.getMIPSLabel());
                MipsController.getInstance().addAsm(bne);
                BranchITAsm beq = new BranchITAsm(BranchITAsm.Op.beq, judge.getRegister(), Register.ZERO, label2.getMIPSLabel());
                MipsController.getInstance().addAsm(beq);
            } else if (judge instanceof ConstInteger constInteger) {
                int v = constInteger.getVal();
                LiAsm li = new LiAsm(Register.K0, v);
                MipsController.getInstance().addAsm(li);
                BranchITAsm bne = new BranchITAsm(BranchITAsm.Op.bne, Register.K0, Register.ZERO, label1.getMIPSLabel());
                MipsController.getInstance().addAsm(bne);
                BranchITAsm beq = new BranchITAsm(BranchITAsm.Op.beq, Register.K0, Register.ZERO, label2.getMIPSLabel());
                MipsController.getInstance().addAsm(beq);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, judge.getOffset());
                MipsController.getInstance().addAsm(lw);
                BranchITAsm bne = new BranchITAsm(BranchITAsm.Op.bne, Register.K0, Register.ZERO, label1.getMIPSLabel());
                MipsController.getInstance().addAsm(bne);
                BranchITAsm beq = new BranchITAsm(BranchITAsm.Op.beq, Register.K0, Register.ZERO, label2.getMIPSLabel());
                MipsController.getInstance().addAsm(beq);
            }
        }
    }

    public ArrayList<BasicBlock> getSuccessors() {
        ArrayList<BasicBlock> successors = new ArrayList<>();
        successors.add(label1);
        if (label2 != null) successors.add(label2);
        return successors;
    }
}
