package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class FormatString extends Node {
    public FormatString(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
