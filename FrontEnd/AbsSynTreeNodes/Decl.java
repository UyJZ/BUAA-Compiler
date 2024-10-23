package FrontEnd.AbsSynTreeNodes;

import java.util.ArrayList;

public class Decl extends SynTreeNode {
    public Decl(SyntaxVarType type, ArrayList<SynTreeNode> children){
        super(type, children);
    }
}
