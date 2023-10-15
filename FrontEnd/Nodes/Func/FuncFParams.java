package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncFParams extends Node {

    private final ArrayList<FuncFParam> paramList = new ArrayList<>();

    public FuncFParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node child : children) {
            if (child instanceof FuncFParam) {
                paramList.add((FuncFParam) child);
            }
        }
    }

    public ArrayList<FuncFParam> getParamList() {
        return paramList;
    }
}
