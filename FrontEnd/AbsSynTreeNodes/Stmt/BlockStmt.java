package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;

import java.util.ArrayList;

public class BlockStmt extends Stmt {
    public BlockStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
