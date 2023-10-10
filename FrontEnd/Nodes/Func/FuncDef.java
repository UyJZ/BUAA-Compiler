package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class FuncDef extends Node {
    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
