package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class UnaryExp extends Node {
    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
