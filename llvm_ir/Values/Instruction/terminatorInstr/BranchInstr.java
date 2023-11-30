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
import java.util.HashMap;

public class BranchInstr extends Instr {

    private final BasicBlock label1, label2;

    public BranchInstr(BasicBlock label1, BasicBlock label2, Value judge) {
        super(new LLVMType(), "");
        this.label1 = label1;
        this.label2 = label2;
        this.addValue(judge);
    }

    public BranchInstr(BasicBlock label1) {
        super(new LLVMType(), "");
        this.label1 = label1;
        this.label2 = null;
    }

    @Override
    public String toString() {
        if (operands.size() == 0)
            return "br label " + label1.getName();
        return "br " + new BoolType().toString() + " " + operands.get(0).getName() + ", label " + label1.getName() + ", label " + label2.getName();
    }

    @Override
    public void genMIPS() {
        CommentAsm asm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(asm);
        if (operands.size() == 0) {
            JAsm j = new JAsm(label1.getMIPSLabel());
            MipsController.getInstance().addAsm(j);
        } else {
            Value judge = operands.get(0);
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

    @Override
    public Instr copy(HashMap<Value, Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        if (operands.size() == 0) {
            return new BranchInstr((BasicBlock) label1.copy(map));
        } else {
            return new BranchInstr((BasicBlock) label1.copy(map), (BasicBlock) label2.copy(map), operands.get(0).copy(map));
        }
    }
}
