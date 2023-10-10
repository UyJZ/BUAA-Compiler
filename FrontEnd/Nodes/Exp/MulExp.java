package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class MulExp extends Node{
    public MulExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
