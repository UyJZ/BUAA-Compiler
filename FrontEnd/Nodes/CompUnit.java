package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class CompUnit extends Node{
    public CompUnit(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
