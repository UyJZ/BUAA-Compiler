package FrontEnd.AbsSynTreeNodes;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Exp.LOrExp;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.BasicBlock;

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
    public LLVM_Value genLLVMir() {
        ((LOrExp) children.get(0)).setBlock(falseBlock, trueBlock);
        super.genLLVMir();
        return null;
    }
}
