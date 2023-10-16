package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class AddExp extends Node {
    public AddExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public int getDim() {
        for (Node n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }
}
