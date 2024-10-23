package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.IcmpInstr;
import Ir_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;
import Ir_LLVM.LLVM_Types.BoolType;

import java.util.ArrayList;

public class LAndExp extends Node {

    private BasicBlock newBasicBlock, trueBlock, falseBlock;

    public LAndExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock newBasicBlock, BasicBlock trueBlock, BasicBlock falseBlock) {
        this.newBasicBlock = newBasicBlock;
        this.falseBlock = falseBlock;
        this.trueBlock = trueBlock;
    }

    public void setBlock(BasicBlock trueBlock, BasicBlock falseBlock) {
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (newBasicBlock != null)
            LLVM_Builder.getInstance().addNewBasicBlock(newBasicBlock);
        if (children.size() == 1) {
            ((EqExp) children.get(0)).setBlock(null, trueBlock, falseBlock);
            LLVM_Value v = children.get(0).genLLVMir();
            return ToBool(v);
        } else {
            BasicBlock newBlock = new BasicBlock();
            ((LAndExp) children.get(0)).setBlock(newBlock, falseBlock);
            ((EqExp) children.get(2)).setBlock(newBlock, trueBlock, falseBlock);
            children.get(0).genLLVMir();
            LLVM_Value v = children.get(2).genLLVMir();
            return ToBool(v);
        }
    }

    private LLVM_Value ToBool(LLVM_Value v) {
        if (v instanceof ConstInteger constInteger) {
            BranchInstr branchInstr;
            if (constInteger.getVal() == 0) {
                branchInstr = new BranchInstr(falseBlock);
            } else {
                branchInstr = new BranchInstr(trueBlock);
            }
            LLVM_Builder.getInstance().addInstr(branchInstr);
            return null;
        }
        if (!(v.getType() instanceof BoolType)) {
            IcmpInstr icmpInstr = new IcmpInstr(v, new ConstInteger(0), IcmpInstr.CmpOp.ne);
            LLVM_Builder.getInstance().addInstr(icmpInstr);
            BranchInstr branchInstr = new BranchInstr(trueBlock, falseBlock, icmpInstr);
            LLVM_Builder.getInstance().addInstr(branchInstr);
            return null;
        }
        BranchInstr branchInstr = new BranchInstr(trueBlock, falseBlock, v);
        LLVM_Builder.getInstance().addInstr(branchInstr);
        return null;
    }
}
