package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;

import java.util.ArrayList;

public class BlockStmt extends Stmt {
    public BlockStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }
}
