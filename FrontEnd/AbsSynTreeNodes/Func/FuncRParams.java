package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class FuncRParams extends SynTreeNode {
    public FuncRParams(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public ArrayList<Integer> getDims() {
        ArrayList<Integer> dims = new ArrayList<>();
        for (SynTreeNode n : children)
            if (n instanceof Exp) dims.add(((Exp) n).getDim());
        return dims;
    }

    @Override
    public void checkError() {
        super.checkError();
    }

    public ArrayList<LLVM_Value> genLLVMirForFunc() {
        ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
        for (SynTreeNode n : children) {
            if (n instanceof Exp) {
                LLVM_Value LLVMValue = n.genLLVMir();
                LLVMValues.add(LLVMValue);
            }
        }
        return LLVMValues;
    }
}
