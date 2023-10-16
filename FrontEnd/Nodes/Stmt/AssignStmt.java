package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.LVal;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.*;

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

    @Override
    public void checkError() {
        super.checkError();
        VarSymbol varSymbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(lVal.getName());
        if (varSymbol != null && varSymbol.isConst()) ErrorChecker.AddError(new Error(lVal.identLine(), ErrorType.h));
    }
}
