package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Values.BasicBlock;

import java.util.ArrayList;

public class Stmt extends SynTreeNode {

    protected BasicBlock Stmt2Block, NextBlock;

    public Stmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock Stmt2Block, BasicBlock nextBlock) {
        this.Stmt2Block = Stmt2Block;
        NextBlock = nextBlock;
    }
}
