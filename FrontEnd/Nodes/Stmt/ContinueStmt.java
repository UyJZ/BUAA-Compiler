package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ContinueStmt extends Stmt {
    public ContinueStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
