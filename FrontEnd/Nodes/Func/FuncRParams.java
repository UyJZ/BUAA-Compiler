package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;

public class FuncRParams extends Node {
    public FuncRParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<Integer> getDims() {
        ArrayList<Integer> dims = new ArrayList<>();
        for (Node n : children)
            if (n instanceof Exp) dims.add(((Exp) n).getDim());
        return dims;
    }

    @Override
    public void checkError() {
        super.checkError();
    }
}
