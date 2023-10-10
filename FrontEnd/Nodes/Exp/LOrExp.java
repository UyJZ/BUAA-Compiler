package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class LOrExp extends Node {
    public LOrExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
