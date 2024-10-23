package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;

import java.util.ArrayList;

public class LOrExp extends SynTreeNode {

    private BasicBlock trueBlock, falseBlock;

    public LOrExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock falseBlock, BasicBlock trueBlock) {
        this.falseBlock = falseBlock;
        this.trueBlock = trueBlock;
    }

    @Override
    public LLVM_Value genLLVMir() {
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
