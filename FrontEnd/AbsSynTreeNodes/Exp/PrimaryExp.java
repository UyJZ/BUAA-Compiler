package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.Var.Number;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class PrimaryExp extends SynTreeNode {
    public PrimaryExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public int getDim() {
        for (SynTreeNode n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }

    public int calc() {
        if (children.size() == 1 && children.get(0) instanceof Number) return ((Number) children.get(0)).calc();
        else if (children.size() == 1 && children.get(0) instanceof LVal) return ((LVal) children.get(0)).calc();
        else if (children.size() == 3) {
            return ((Exp) children.get(1)).calc();
        } else return -1;
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (children.size() == 1 && children.get(0) instanceof Number) return ((Number) children.get(0)).genLLVMir();
        else if (children.size() == 1 && children.get(0) instanceof LVal) return ((LVal) children.get(0)).genLLVMir();
        else if (children.size() == 3) {
            return children.get(1).genLLVMir();
        } else return null;
    }
}
