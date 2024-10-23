package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;

import java.util.ArrayList;

public class FuncType extends SynTreeNode {
    public FuncType(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public String getValue() {
        return ((TokenSynTreeNode)children.get(0)).getValue();
    }
}
