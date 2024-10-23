package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;

import java.util.ArrayList;

public class FuncFParams extends SynTreeNode {

    private final ArrayList<FuncFParam> paramList = new ArrayList<>();

    public FuncFParams(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        for (SynTreeNode child : children) {
            if (child instanceof FuncFParam) {
                paramList.add((FuncFParam) child);
            }
        }
    }

    public ArrayList<FuncFParam> getParamList() {
        return paramList;
    }

    public void setParamLLVMForFunc() {
        for (SynTreeNode n : children) {
            if (n instanceof FuncFParam) {
                ((FuncFParam) n).setParamLLVMForFunc();
            }
        }
    }
}
