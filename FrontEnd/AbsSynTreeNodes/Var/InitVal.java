package FrontEnd.AbsSynTreeNodes.Var;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import FrontEnd.Lexer.Token;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import IR_LLVM.InitializedValue;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class InitVal extends SynTreeNode {
    public InitVal(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public InitializedValue getVal() {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        if (children.get(0) instanceof Exp) {
            list.add(new ArrayList<>());
            list.get(0).add(((Exp) children.get(0)).calc());
            return new InitializedValue(0, list);
        } else {
            if (children.get(0) instanceof TokenSynTreeNode && children.get(1) instanceof InitVal
                    && ((TokenSynTreeNode) children.get(0)).getTokenType() == Token.TokenType.LBRACE &&
                    ((InitVal)children.get(1)).getVal().getDim() == 1) {
                InitializedValue initializedValue = new InitializedValue(2, list);
                for (SynTreeNode n : children) {
                    if (n instanceof InitVal) {
                        initializedValue.addInitial(((InitVal) n).getVal());
                    }
                }
                return initializedValue;
            } else {
                list.add(new ArrayList<>());
                InitializedValue initializedValue = new InitializedValue(1, list);
                for (SynTreeNode n : children) {
                    if (n instanceof InitVal) {
                        initializedValue.addInitial(((InitVal) n).getVal());
                    }
                }
                return initializedValue;
            }
        }
    }

    public int getDim() {
        if (children.get(0) instanceof Exp) return 0;
        else {
            for (SynTreeNode n : children) {
                if (n instanceof InitVal) return n.getDim() + 1;
            }
        }
        return -1;
    }

    public ArrayList<ArrayList<LLVM_Value>> genLLVMirListFor2Dim() {
        assert (getDim() == 2);
        ArrayList<ArrayList<LLVM_Value>> valuesList = new ArrayList<>();
        for (SynTreeNode n : children) {
            if (n instanceof InitVal) {
                valuesList.add(((InitVal) n).genLLVMirListFor1Dim());
            }
        }
        return valuesList;
    }

    public ArrayList<LLVM_Value> genLLVMirListFor1Dim() {
        assert (getDim() == 1);
        ArrayList<LLVM_Value> LLVMValueArrayList = new ArrayList<>();
        for (SynTreeNode n : children) {
            if (n instanceof InitVal) {
                LLVMValueArrayList.add( n.genLLVMir());
            }
        }
        return LLVMValueArrayList;
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (children.size() == 1) return children.get(0).genLLVMir();
        else return null;
    }
}
