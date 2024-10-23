package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import Ir_LLVM.LLVM_Values.BasicBlock;

import java.util.ArrayList;

public class Stmt extends Node {

    protected BasicBlock Stmt2Block, NextBlock;

    public Stmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock Stmt2Block, BasicBlock nextBlock) {
        this.Stmt2Block = Stmt2Block;
        NextBlock = nextBlock;
    }
}
