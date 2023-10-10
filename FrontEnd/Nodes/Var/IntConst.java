package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class IntConst extends Node {
    public IntConst(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
