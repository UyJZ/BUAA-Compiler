package FrontEnd.AbsSynTreeNodes;

import FrontEnd.Parser.FunctionType;
import FrontEnd.AbsSynTreeNodes.Stmt.ReturnStmt;

import java.util.ArrayList;

public class BlockItem extends SynTreeNode {
    public BlockItem(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public FunctionType getReturnType() {
        if (!(children.get(0) instanceof ReturnStmt)) return null;
        return ((ReturnStmt)children.get(0)).getReturnType();
    }

    public boolean isReturnStmt() {
        return children.get(0) instanceof ReturnStmt;
    }
}
