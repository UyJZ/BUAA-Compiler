package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ConstDef extends Node {
    public ConstDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
