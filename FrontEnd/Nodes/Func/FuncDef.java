package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncDef extends Node {

    private ArrayList<FuncFParam> funcFParams;

    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        //TODO: fill the funcFParams
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block,push this funcDef into symbolTable, insert the funcFParams
        super.checkError();
        //TODO:Leave the symbol table
    }
}
