package FrontEnd.AbsSynTreeNodes.Var;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;

import java.util.ArrayList;

public class IntConst extends SynTreeNode {
    public IntConst(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public int calc() {
        return Integer.parseInt(((TokenSynTreeNode)children.get(0)).getValue());
    }
}
