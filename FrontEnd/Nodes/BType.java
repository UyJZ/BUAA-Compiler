package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class BType extends Node{
    public BType(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
