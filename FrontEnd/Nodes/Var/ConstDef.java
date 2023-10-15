package FrontEnd.Nodes.Var;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;

import java.util.ArrayList;

public class ConstDef extends Node {

    private int dim = 0;

    private ArrayList<Integer> initValue;

    private String name;

    public ConstDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(1)).toString();
        for (Node child : children) {
            if (child instanceof ConstDecl) {
                dim++;
            }
        }
    }

    @Override
    public void checkError() {
        VarSymbol varSymbol = new VarSymbol(name, SymbolType.SYMBOL_VAR, dim, true);
        try {
            SymbolManager.getInstance().addSymbol(varSymbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
    }
}
