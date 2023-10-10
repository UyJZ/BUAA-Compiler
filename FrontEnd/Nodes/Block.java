package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class Block extends Node{
    public Block(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
