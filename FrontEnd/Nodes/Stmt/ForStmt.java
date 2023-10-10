package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ForStmt extends Node {
    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
