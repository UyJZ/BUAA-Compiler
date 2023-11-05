package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

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
    public Value genLLVMir() {
        return new Value(new Integer32Type(), String.valueOf(calc()));
    }
}
