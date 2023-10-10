package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class BlockItem extends Node{
    public BlockItem(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
