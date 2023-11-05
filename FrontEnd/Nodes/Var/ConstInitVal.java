package FrontEnd.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.Initial;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public Initial getVal() {
        if (children.get(0) instanceof ConstExp) {
            ArrayList<ArrayList<Integer>> list = new ArrayList<>();
            list.add(new ArrayList<>());
            list.get(0).add(((ConstExp) children.get(0)).calc());
            return new Initial(0, list);
        } else {
            ArrayList<ArrayList<Integer>> list = new ArrayList<>();
            list.add(new ArrayList<>());
            if (children.get(0) instanceof TokenNode && children.get(1) instanceof TokenNode &&
                    ((TokenNode) children.get(0)).getTokenType() == tokenType.LBRACE &&
                    (((TokenNode) children.get(1)).getTokenType() == tokenType.LBRACE)) {
                Initial initial = new Initial(2, list);
                for (Node n : children) {
                    if (n instanceof ConstInitVal) {
                        initial.addInitial(((ConstInitVal) n).getVal());
                    }
                }
                return initial;
            } else {
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
}
