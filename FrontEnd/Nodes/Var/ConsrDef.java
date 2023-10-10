package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ConsrDef extends Node {
    public ConsrDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
