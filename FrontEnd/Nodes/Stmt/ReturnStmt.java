package FrontEnd.Nodes.Stmt;

import Enums.FunctionType;
import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.Exp.Exp;

import java.util.ArrayList;

public class ReturnStmt extends Stmt {
    public ReturnStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FunctionType getReturnType() {
        for (Node n : children) {
            if (n instanceof Exp) {
                return FunctionType.FUNC_INT;
            }
        }
        return FunctionType.FUNC_VOID;
    }
}
