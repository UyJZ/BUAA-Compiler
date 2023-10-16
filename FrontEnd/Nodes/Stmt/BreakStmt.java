package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public BreakStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        if (SymbolManager.getInstance().getLoopDepth() == 0)
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.m));
    }
}
