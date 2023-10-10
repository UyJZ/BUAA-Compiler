package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class Ident extends Node {
    public Ident(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
