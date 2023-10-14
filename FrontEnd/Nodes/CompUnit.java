package FrontEnd.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class CompUnit extends Node{
    public CompUnit(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block
        super.checkError();
        //TODO:Leave the symbol table
    }
}
