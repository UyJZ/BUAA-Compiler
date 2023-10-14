package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ForLoopStmt extends Stmt {
    public ForLoopStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
