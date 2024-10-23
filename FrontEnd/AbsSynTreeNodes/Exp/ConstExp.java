package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import Ir_LLVM.LLVM_Value;

import java.util.ArrayList;

public class ConstExp extends Node {
    public ConstExp(SyntaxVarType type, ArrayList<Node> children) {
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
