package FrontEnd.AbsSynTreeNodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;

import java.util.ArrayList;

public class FuncType extends Node {
    public FuncType(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public String getValue() {
        return ((TokenNode)children.get(0)).getValue();
    }
}
