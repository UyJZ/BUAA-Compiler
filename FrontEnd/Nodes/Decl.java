package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class Decl extends Node{
    public Decl(SyntaxVarType type, ArrayList<Node> children){
        super(type, children);
    }
}
