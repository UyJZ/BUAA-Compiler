package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import llvm_ir.Value;

import java.util.ArrayList;

public class ConstExp extends Node {
    public ConstExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public int calc() {
        return ((AddExp) children.get(0)).calc();
    }

    @Override
    public Value genLLVMir() {
        return children.get(0).genLLVMir();
    }
}
