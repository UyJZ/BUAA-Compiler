package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class RelExp extends Node {
    public RelExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
