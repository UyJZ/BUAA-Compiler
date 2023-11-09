package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.Initial;
import llvm_ir.Value;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public Initial getVal() {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        if (children.get(0) instanceof ConstExp) {
            list.add(new ArrayList<>());
            list.get(0).add(((ConstExp) children.get(0)).calc());
            return new Initial(0, list);
        } else {
            if (children.get(0) instanceof TokenNode && children.get(1) instanceof ConstInitVal &&
                    ((TokenNode) children.get(0)).getTokenType() == tokenType.LBRACE &&
                    ((ConstInitVal)children.get(1)).getVal().getDim() == 1) {
                Initial initial = new Initial(2, list);
                for (Node n : children) {
                    if (n instanceof ConstInitVal) {
                        initial.addInitial(((ConstInitVal) n).getVal());
                    }
                }
                return initial;
            } else {
                list.add(new ArrayList<>());
                Initial initial = new Initial(1, list);
                for (Node n : children) {
                    if (n instanceof ConstInitVal) {
                        initial.addInitial(((ConstInitVal) n).getVal());
                    }
                }
                return initial;
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

    public ArrayList<ArrayList<Value>> genLLVMirListFor2Dim() {
        assert (getDim() == 2);
        ArrayList<ArrayList<Value>> valuesList = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof ConstInitVal) {
                valuesList.add(((ConstInitVal) n).genLLVMirListFor1Dim());
            }
        }
        return valuesList;
    }

    public ArrayList<Value> genLLVMirListFor1Dim() {
        assert (getDim() == 1);
        ArrayList<Value> valueArrayList = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof ConstInitVal) {
                valueArrayList.add( n.genLLVMir());
            }
        }
        return valueArrayList;
    }

    @Override
    public Value genLLVMir() {
        if (children.size() == 1) return children.get(0).genLLVMir();
        else return null;
    }
}
