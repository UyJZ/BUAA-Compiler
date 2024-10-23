package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.Var.Number;
import Ir_LLVM.LLVM_Value;

import java.util.ArrayList;

public class PrimaryExp extends Node {
    public PrimaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public int getDim() {
        for (Node n : children) if (n.getDim() != -1) return n.getDim();
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
