package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;

import java.util.ArrayList;

public class ExpStmt extends Stmt {
    private Exp exp;

    public ExpStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        for (SynTreeNode synTreeNode : children) {
            if (synTreeNode instanceof Exp) exp = (Exp) synTreeNode;
        }
    }
}
