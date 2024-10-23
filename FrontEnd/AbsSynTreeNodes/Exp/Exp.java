package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class Exp extends SynTreeNode {
    public Exp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public int getDim() {
        for (SynTreeNode n : children) {
            if (n.getDim() != -1) return n.getDim();
        }
        return -1;
    }

    public int calc() {
        return ((AddExp) children.get(0)).calc();
    }

    @Override
    public LLVM_Value genLLVMir() {
        return children.get(0).genLLVMir();
    }
}
