package FrontEnd.Nodes.Func;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;

import java.util.ArrayList;

public class FuncFParam extends Node {

    public FuncFParam(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public String getName() {
        return ((TokenNode) children.get(1)).getIdentName();
    }

    public int getDim() {
        int dim = 0;
        if (children.size() == 2) return dim;
        if (children.get(2) instanceof TokenNode && ((TokenNode) children.get(2)).getTokenType() == tokenType.LBRACK) dim++;
        for (Node n : children) if (n instanceof ConstExp) dim++;
        return dim;
    }

    @Override
    public void checkError() {
        try {
            SymbolManager.getInstance().addSymbol(new VarSymbol(getName(), SymbolType.SYMBOL_VAR, getDim(), false));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        super.checkError();
    }
}
