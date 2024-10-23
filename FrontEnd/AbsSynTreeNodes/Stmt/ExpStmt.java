package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.Node;

import java.util.ArrayList;

public class ExpStmt extends Stmt {
    private Exp exp;

    public ExpStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node node : children) {
            if (node instanceof Exp) exp = (Exp) node;
        }
    }
}
