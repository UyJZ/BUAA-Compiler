package FrontEnd.Nodes.Stmt;

import Enums.FuncType;
import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ReturnStmt extends Stmt {
    public ReturnStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FuncType getReturnType() {
        return children.size() > 2 ? FuncType.FUNC_INT : FuncType.FUNC_VOID;
    }
}
