package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class Number extends Node {
    public Number(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
