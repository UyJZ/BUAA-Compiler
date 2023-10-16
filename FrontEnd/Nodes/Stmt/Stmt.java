package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;

import java.io.PrintStream;
import java.util.ArrayList;

public class Stmt extends Node {
    public Stmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
