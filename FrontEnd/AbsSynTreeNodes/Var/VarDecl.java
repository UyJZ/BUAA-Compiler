package FrontEnd.AbsSynTreeNodes.Var;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;

import java.util.ArrayList;

public class VarDecl extends SynTreeNode {
    public VarDecl(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }
}
