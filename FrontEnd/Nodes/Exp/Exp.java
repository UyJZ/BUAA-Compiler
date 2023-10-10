package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class Exp extends Node {
    public Exp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
