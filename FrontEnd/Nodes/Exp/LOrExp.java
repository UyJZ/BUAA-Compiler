package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.BoolType;

import java.util.ArrayList;

public class LOrExp extends Node {

    private BasicBlock trueBlock, falseBlock;

    public LOrExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock falseBlock, BasicBlock trueBlock) {
        this.falseBlock = falseBlock;
        this.trueBlock = trueBlock;
    }

    @Override
    public Value genLLVMir() {
        if (children.size() == 1) {
            ((LAndExp)children.get(0)).setBlock(trueBlock, falseBlock);
            children.get(0).genLLVMir();
        } else {
            BasicBlock newBlock = new BasicBlock();
            ((LOrExp) children.get(0)).setBlock(newBlock, trueBlock);
            ((LAndExp) children.get(2)).setBlock(newBlock, trueBlock, falseBlock);
            super.genLLVMir();
        }
        return null;
    }
}
