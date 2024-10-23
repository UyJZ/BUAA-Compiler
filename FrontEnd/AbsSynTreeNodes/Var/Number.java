package FrontEnd.AbsSynTreeNodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.ConstInteger;

import java.util.ArrayList;

public class Number extends Node {
    public Number(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public int calc() {
        return ((IntConst) children.get(0)).calc();
    }

    public int getDim() {
        return 0;
    }

    @Override
    public LLVM_Value genLLVMir() {
        return new ConstInteger(calc());
    }
}
