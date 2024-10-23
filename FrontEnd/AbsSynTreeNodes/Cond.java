package FrontEnd.AbsSynTreeNodes;

import FrontEnd.AbsSynTreeNodes.Exp.LOrExp;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;

import java.util.ArrayList;

public class Cond extends SynTreeNode {

    private BasicBlock trueBlock, falseBlock;

    public Cond(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock trueBlock, BasicBlock falseBlock) {
        this.falseBlock = falseBlock;
        this.trueBlock = trueBlock;
    }

    @Override
    public LLVM_Value genLLVMir() {
        ((LOrExp) children.get(0)).setBlock(falseBlock, trueBlock);
        super.genLLVMir();
        return null;
    }
}
