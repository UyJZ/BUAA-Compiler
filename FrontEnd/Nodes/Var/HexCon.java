package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class HexCon extends Node {
    public HexCon(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}