package FrontEnd.Nodes;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Exp.LOrExp;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;

import java.util.ArrayList;

public class Cond extends Node {

    private BasicBlock trueBlock, falseBlock;

    public Cond(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock trueBlock, BasicBlock falseBlock) {
        this.falseBlock = falseBlock;
        this.trueBlock = trueBlock;
    }

    @Override
    public Value genLLVMir() {
        ((LOrExp) children.get(0)).setBlock(falseBlock, trueBlock);
        super.genLLVMir();
        return null;
    }
}
