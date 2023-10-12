package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncRParams extends Node {
    public FuncRParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
