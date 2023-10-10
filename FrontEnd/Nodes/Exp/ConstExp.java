package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ConstExp extends Node {
    public ConstExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
