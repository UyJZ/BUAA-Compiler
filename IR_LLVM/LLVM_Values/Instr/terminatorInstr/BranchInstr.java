package IR_LLVM.LLVM_Values.Instr.terminatorInstr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Instr.Instr;
import IR_LLVM.LLVM_Types.BoolType;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;
import java.util.HashMap;

public class BranchInstr extends Instr {

    public BranchInstr(BasicBlock label1, BasicBlock label2, LLVM_Value judge) {
        super(new LLVMType(), "");
        this.addValue(judge);
        this.addValue(label1);
        this.addValue(label2);
    }

    public BranchInstr(BasicBlock label1) {
        super(new LLVMType(), "");
        this.addValue(label1);
    }

    @Override
    public String toString() {
        if (operands.size() == 1)
            return "br label " + operands.get(0).getName();
        return "br " + new BoolType().toString() + " " + operands.get(0).getName() + ", label " + operands.get(1).getName() + ", label " + operands.get(2).getName();
    }

    @Override
    public void genMIPS() {
        CommentAsm asm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(asm);
        ArrayList<BasicBlock> blocks = new ArrayList<>(MipsBuilder.getInstance().getCurrentFunction().getBlockArrayList());
        BasicBlock currentBlock = MipsBuilder.getInstance().getCurrentBlock();
        if (operands.size() == 1) {
            BasicBlock label1 = (BasicBlock) operands.get(0);
            if (blocks.indexOf(currentBlock) + 1 == blocks.indexOf(label1)) {
                return;
            }
            JAsm j = new JAsm(label1.getMIPSLabel());
            MipsBuilder.getInstance().addAsm(j);
        } else {
            BasicBlock label1 = (BasicBlock) operands.get(1);
            BasicBlock label2 = (BasicBlock) operands.get(2);
            LLVM_Value judge = operands.get(0);
            if (judge.isUseReg() && judge.getRegister() != Register.ZERO) {
                if (blocks.indexOf(currentBlock) + 1 != blocks.indexOf(label1)) {
                    BranchITAsm bne = new BranchITAsm(BranchITAsm.Op.bne, judge.getRegister(), Register.ZERO, label1.getMIPSLabel());
                    MipsBuilder.getInstance().addAsm(bne);
                }
                if (blocks.indexOf(currentBlock) + 1 != blocks.indexOf(label2)) {
                    BranchITAsm beq = new BranchITAsm(BranchITAsm.Op.beq, judge.getRegister(), Register.ZERO, label2.getMIPSLabel());
                    MipsBuilder.getInstance().addAsm(beq);
                }
            } else if (judge instanceof ConstInteger constInteger) {
                int v = constInteger.getVal();
                JAsm j;
                if (v == 0) {
                    if (blocks.indexOf(currentBlock) + 1 == blocks.indexOf(label2)) {
                        return;
                    }
                    j = new JAsm(label2.getMIPSLabel());
                } else {
                    if (blocks.indexOf(currentBlock) + 1 == blocks.indexOf(label1)) {
                        return;
                    }
                    j = new JAsm(label1.getMIPSLabel());
                }
                MipsBuilder.getInstance().addAsm(j);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, judge.getOffset());
                MipsBuilder.getInstance().addAsm(lw);
                if (blocks.indexOf(currentBlock) + 1 != blocks.indexOf(label1)) {
                    BranchITAsm bne = new BranchITAsm(BranchITAsm.Op.bne, Register.K0, Register.ZERO, label1.getMIPSLabel());
                    MipsBuilder.getInstance().addAsm(bne);
                }
                if (blocks.indexOf(currentBlock) + 1 != blocks.indexOf(label2)) {
                    BranchITAsm beq = new BranchITAsm(BranchITAsm.Op.beq, Register.K0, Register.ZERO, label2.getMIPSLabel());
                    MipsBuilder.getInstance().addAsm(beq);
                }
            }
        }
    }

    public ArrayList<BasicBlock> getSuccessors() {
        ArrayList<BasicBlock> successors = new ArrayList<>();
        if (operands.size() == 1) {
            BasicBlock label1 = (BasicBlock) operands.get(0);
            successors.add(label1);
        } else {
            BasicBlock label1 = (BasicBlock) operands.get(1);
            BasicBlock label2 = (BasicBlock) operands.get(2);
            successors.add(label1);
            successors.add(label2);
        }
        return successors;
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        if (operands.size() == 1) {
            BasicBlock label1 = (BasicBlock) operands.get(0);
            return new BranchInstr((BasicBlock) label1.copy(map));
        } else {
            BasicBlock label1 = (BasicBlock) operands.get(1);
            BasicBlock label2 = (BasicBlock) operands.get(2);
            return new BranchInstr((BasicBlock) label1.copy(map), (BasicBlock) label2.copy(map), operands.get(0).copy(map));
        }
    }

    @Override
    public boolean isPinnedInst() {
        return true;
    }

    public LLVM_Value getJudge() {
        return operands.get(0);
    }
}
