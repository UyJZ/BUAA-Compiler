package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class LAndExp extends Node {
    public LAndExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
