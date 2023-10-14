package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    public IfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
