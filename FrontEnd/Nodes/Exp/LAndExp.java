package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.BoolType;
import llvm_ir.llvmType.LLVMType;

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
    public Value genLLVMir() {
        if (newBasicBlock != null)
            IRController.getInstance().addNewBasicBlock(newBasicBlock);
        if (children.size() == 1) {
            ((EqExp)children.get(0)).setBlock(null, trueBlock, falseBlock);
            Value v = children.get(0).genLLVMir();
            return ToBool(v);
        } else {
            BasicBlock newBlock = new BasicBlock();
            ((LAndExp) children.get(0)).setBlock(newBlock, falseBlock);
            ((EqExp) children.get(2)).setBlock(newBlock, trueBlock, falseBlock);
            children.get(0).genLLVMir();
            Value v = children.get(2).genLLVMir();
            return ToBool(v);
        }
    }

    private Value ToBool(Value v) {
        if (!(v.getType() instanceof BoolType)) {
            IcmpInstr icmpInstr = new IcmpInstr(v, new ConstInteger(0), IcmpInstr.CmpOp.ne);
            IRController.getInstance().addInstr(icmpInstr);
            BranchInstr branchInstr = new BranchInstr(trueBlock, falseBlock, icmpInstr);
            IRController.getInstance().addInstr(branchInstr);
            return null;
        }
        BranchInstr branchInstr = new BranchInstr(trueBlock, falseBlock, v);
        IRController.getInstance().addInstr(branchInstr);
        return null;
    }
}
