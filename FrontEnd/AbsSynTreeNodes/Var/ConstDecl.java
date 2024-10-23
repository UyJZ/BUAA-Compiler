package FrontEnd.AbsSynTreeNodes.Var;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;

import java.util.ArrayList;

public class ConstDecl extends SynTreeNode {

    private int dim = 0;

    private ArrayList<Integer> initValue;

    private String name;

    public ConstDecl(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        name = ((ConstDef) children.get(2)).getName();
        for (SynTreeNode child : children) {
            if (child instanceof ConstDecl) {
                dim++;
            }
        }
    }

}
