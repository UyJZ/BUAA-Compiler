package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncFParams extends Node {
    public FuncFParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
