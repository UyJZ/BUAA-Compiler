package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class Cond extends Node{
    public Cond(SyntaxVarType type, ArrayList<Node> children){
        super(type, children);
    }
}
