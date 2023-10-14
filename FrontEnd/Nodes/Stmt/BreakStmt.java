package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public BreakStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
