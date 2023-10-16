package FrontEnd.Nodes;

import Enums.FunctionType;
import Enums.SyntaxVarType;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;

public class Block extends Node{
    public Block(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().enterBlock();
        super.checkError();
        SymbolManager.getInstance().leaveBlock();
    }

    public void checkErrorInFunc() {
        super.checkError();
    }

    public boolean isReturnIntInFunc() {
        for (Node n : children) {
            if (n instanceof BlockItem && ((BlockItem) n).getReturnType() == FunctionType.FUNC_INT) return true;
        }
        return false;
    }

    public ArrayList<Integer> getReturnIntLine() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof BlockItem && ((BlockItem) n).getReturnType() == FunctionType.FUNC_INT) ret.add(n.getStartLine());
        }
        return ret;
    }

    public boolean isLastStmtReturnInt() {
        if (children.get(children.size() - 2) instanceof BlockItem)
            return ((BlockItem) children.get(children.size() - 2)).getReturnType() == FunctionType.FUNC_INT;
        return false;
    }
}
