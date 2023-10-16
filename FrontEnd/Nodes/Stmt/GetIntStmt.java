package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.LVal;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;

import java.util.ArrayList;

public class GetIntStmt extends Stmt {

    private LVal lVal;
    public GetIntStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node node : children) if (node instanceof LVal) lVal = (LVal) node;
    }

    @Override
    public void checkError() {
        VarSymbol varSymbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(lVal.getName());
        if (varSymbol.isConst()) ErrorChecker.AddError(new Error(lVal.identLine(), ErrorType.h));
        super.checkError();
    }
}
