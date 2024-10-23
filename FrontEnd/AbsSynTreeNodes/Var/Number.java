package FrontEnd.AbsSynTreeNodes.Var;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.ConstInteger;

import java.util.ArrayList;

public class Number extends SynTreeNode {
    public Number(SyntaxVarType type, ArrayList<SynTreeNode> children) {
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
