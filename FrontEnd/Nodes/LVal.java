package FrontEnd.Nodes;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Nodes.Exp.Exp;

import java.util.ArrayList;

public class LVal extends Node {

    private final String name;

    public LVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
    }

    public int identLine() {
        return children.get(0).getEndLine();
    }

    public String getName() {
        return name;
    }

    @Override
    public int getDim() {
        int dim = SymbolManager.getInstance().getDimByName(name);
        for (Node child : children) if (child instanceof Exp) dim--;
        return dim;
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getTokenType() == tokenType.IDENFR)
            if (!SymbolManager.getInstance().isVarDefined(((TokenNode) children.get(0)).getIdentName()))
                ErrorChecker.AddError(new Error(identLine(), ErrorType.c));
        super.checkError();
    }
}
