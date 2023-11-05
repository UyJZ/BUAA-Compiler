package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;

import java.util.ArrayList;

public class IntConst extends Node {
    public IntConst(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public int calc() {
        return Integer.parseInt(((TokenNode)children.get(0)).getValue());
    }
}
