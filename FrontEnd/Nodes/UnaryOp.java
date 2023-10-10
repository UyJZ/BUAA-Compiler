package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class UnaryOp extends Node {
    public UnaryOp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
