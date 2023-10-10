package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncFParam extends Node {
    public FuncFParam(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
