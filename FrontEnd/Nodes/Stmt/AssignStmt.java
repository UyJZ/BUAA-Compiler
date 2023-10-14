package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.LVal;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class AssignStmt extends Stmt {

    private LVal lVal;

    private Exp expr;

    public AssignStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node node : children) {
            if (node instanceof LVal) lVal = (LVal) node;
            else if (node instanceof Exp) expr = (Exp) node;
        }
    }
}
