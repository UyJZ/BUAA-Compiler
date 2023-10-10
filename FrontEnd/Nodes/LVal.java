package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class LVal extends Node {
    public LVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
