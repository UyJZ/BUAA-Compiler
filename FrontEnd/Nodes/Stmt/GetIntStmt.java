package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class GetIntStmt extends Stmt {
    public GetIntStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
