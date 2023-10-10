package FrontEnd.Nodes.Func;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class MainFuncDef extends Node {
    public MainFuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
