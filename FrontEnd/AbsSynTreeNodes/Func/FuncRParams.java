package FrontEnd.AbsSynTreeNodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.Node;
import Ir_LLVM.LLVM_Value;

import java.util.ArrayList;

public class FuncRParams extends Node {
    public FuncRParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<Integer> getDims() {
        ArrayList<Integer> dims = new ArrayList<>();
        for (Node n : children)
            if (n instanceof Exp) dims.add(((Exp) n).getDim());
        return dims;
    }

    @Override
    public void checkError() {
        super.checkError();
    }

    public ArrayList<LLVM_Value> genLLVMirForFunc() {
        ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof Exp) {
                LLVM_Value LLVMValue = n.genLLVMir();
                LLVMValues.add(LLVMValue);
            }
        }
        return LLVMValues;
    }
}
