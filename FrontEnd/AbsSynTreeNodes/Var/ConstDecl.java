package FrontEnd.AbsSynTreeNodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;

import java.util.ArrayList;

public class ConstDecl extends Node {

    private int dim = 0;

    private ArrayList<Integer> initValue;

    private String name;

    public ConstDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((ConstDef) children.get(2)).getName();
        for (Node child : children) {
            if (child instanceof ConstDecl) {
                dim++;
            }
        }
    }

}
