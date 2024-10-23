package FrontEnd.AbsSynTreeNodes;

import Enums.SyntaxVarType;
import Enums.tokenType;

import java.util.ArrayList;

public class UnaryOp extends Node {
    public UnaryOp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public tokenType getOp() {
        return ((TokenNode) children.get(0)).getTokenType();
    }
}
