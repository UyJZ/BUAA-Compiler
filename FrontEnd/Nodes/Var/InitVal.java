package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class InitVal extends Node {
    public InitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
