package FrontEnd.Nodes.Exp;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Func.FuncRParams;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;
import java.util.Objects;

public class UnaryExp extends Node {
    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        int start = 0;
    }

    @Override
    public void checkError() {
        super.checkError();
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getIdentName() != null) {
            if (!SymbolManager.getInstance().isVarFuncDefined(((TokenNode) children.get(0)).getIdentName())) {
                ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.c));
                return;
            }
            ArrayList<Integer> dims = new ArrayList<>();
            if (children.size() > 2 && children.get(2) instanceof FuncRParams)
                dims = ((FuncRParams) children.get(2)).getDims();
            FuncSymbol symbol = SymbolManager.getInstance().getFuncSymbolByFuncName(((TokenNode) children.get(0)).getIdentName());
            if (symbol.getDimList().size() != dims.size()) {
                ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.d));
            } else for (int i = 0; i < dims.size(); i++) {
                if (!Objects.equals((symbol).getDimList().get(i), dims.get(i))) {
                    ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.e));
                }
            }
        }
    }


    @Override
    public int getDim() {
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getTokenType() == tokenType.IDENFR) {
            return SymbolManager.getInstance().getDimByName(((TokenNode) children.get(0)).getIdentName());
        }
        for (Node n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }
}
