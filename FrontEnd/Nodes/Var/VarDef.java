package FrontEnd.Nodes.Var;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;

import java.util.ArrayList;

public class VarDef extends Node {

    private int dim = 0;
    private final String name;

    private boolean isAssigned;

    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode)children.get(0)).getIdentName();
        isAssigned = false;
        for (Node child : children) {
            if (child.toString().equals("=")) isAssigned = true;
            if (child instanceof ConstExp) dim++;
        }
    }

    @Override
    public void checkError() {
        try {
            SymbolManager.getInstance().addSymbol(new VarSymbol(name, SymbolType.SYMBOL_VAR, dim, false));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
    }
}
