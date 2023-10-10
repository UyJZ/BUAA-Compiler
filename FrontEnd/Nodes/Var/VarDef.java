package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class VarDef extends Node {
    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
