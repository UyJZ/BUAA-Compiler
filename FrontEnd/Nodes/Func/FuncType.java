package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncType extends Node {
    public FuncType(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
