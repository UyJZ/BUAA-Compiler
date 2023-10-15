package FrontEnd.Nodes;

import Enums.FuncType;
import Enums.SyntaxVarType;
import FrontEnd.Nodes.Stmt.ReturnStmt;

import java.util.ArrayList;

public class BlockItem extends Node{
    public BlockItem(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FuncType getReturnType() {
        return ((ReturnStmt)children.get(0)).getReturnType();
    }

    public boolean isReturnStmt() {
        return children.get(0) instanceof ReturnStmt;
    }
}
