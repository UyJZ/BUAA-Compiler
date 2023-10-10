package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class PrimaryExp extends Node {
    public PrimaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
