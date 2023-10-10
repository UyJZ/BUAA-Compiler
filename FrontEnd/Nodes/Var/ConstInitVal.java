package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
