package FrontEnd.AbsSynTreeNodes.Var;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;

import java.util.ArrayList;

public class VarDecl extends Node {
    public VarDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
