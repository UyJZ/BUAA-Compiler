package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class RepeatStmt extends Stmt {
    public RepeatStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
