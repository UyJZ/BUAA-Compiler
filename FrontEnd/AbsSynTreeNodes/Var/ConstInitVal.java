package FrontEnd.AbsSynTreeNodes.Var;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.AbsSynTreeNodes.Exp.ConstExp;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import Ir_LLVM.InitializedValue;
import Ir_LLVM.LLVM_Value;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public InitializedValue getVal() {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        if (children.get(0) instanceof ConstExp) {
            list.add(new ArrayList<>());
            list.get(0).add(((ConstExp) children.get(0)).calc());
            return new InitializedValue(0, list);
        } else {
            if (children.get(0) instanceof TokenNode && children.get(1) instanceof ConstInitVal &&
                    ((TokenNode) children.get(0)).getTokenType() == tokenType.LBRACE &&
                    ((ConstInitVal)children.get(1)).getVal().getDim() == 1) {
                InitializedValue initializedValue = new InitializedValue(2, list);
                for (Node n : children) {
                    if (n instanceof ConstInitVal) {
                        initializedValue.addInitial(((ConstInitVal) n).getVal());
                    }
                }
                return initializedValue;
            } else {
                list.add(new ArrayList<>());
                InitializedValue initializedValue = new InitializedValue(1, list);
                for (Node n : children) {
                    if (n instanceof ConstInitVal) {
                        initializedValue.addInitial(((ConstInitVal) n).getVal());
                    }
                }
                return initializedValue;
            }
        }
    }

    public int getDim() {
        if (children.get(0) instanceof Exp) return 0;
        else {
            for (Node n : children) {
                if (n instanceof ConstInitVal) return n.getDim() + 1;
            }
        }
        return -1;
    }

    public ArrayList<ArrayList<LLVM_Value>> genLLVMirListFor2Dim() {
        assert (getDim() == 2);
        ArrayList<ArrayList<LLVM_Value>> valuesList = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof ConstInitVal) {
                valuesList.add(((ConstInitVal) n).genLLVMirListFor1Dim());
            }
        }
        return valuesList;
    }

    public ArrayList<LLVM_Value> genLLVMirListFor1Dim() {
        assert (getDim() == 1);
        ArrayList<LLVM_Value> LLVMValueArrayList = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof ConstInitVal) {
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
