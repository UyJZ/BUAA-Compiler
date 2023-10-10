package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ConstDecl extends Node {
    public ConstDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
