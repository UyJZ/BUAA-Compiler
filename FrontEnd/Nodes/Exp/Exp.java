package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import llvm_ir.Value;

import java.util.ArrayList;

public class Exp extends Node {
    public Exp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public int getDim() {
        for (Node n : children) {
            if (n.getDim() != -1) return n.getDim();
        }
        return -1;
    }

    public int calc() {
        return ((AddExp) children.get(0)).calc();
    }

    @Override
    public Value genLLVMir() {
        return children.get(0).genLLVMir();
    }
}
