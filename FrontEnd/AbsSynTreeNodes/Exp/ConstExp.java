package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class ConstExp extends SynTreeNode {
    public ConstExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public int calc() {
        return ((AddExp) children.get(0)).calc();
    }

    @Override
    public LLVM_Value genLLVMir() {
        return children.get(0).genLLVMir();
    }
}
