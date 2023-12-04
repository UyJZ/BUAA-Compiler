package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.BinaryInstr;
import llvm_ir.llvmType.Integer32Type;

import java.util.ArrayList;

public class AddExp extends Node {
    public AddExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public int getDim() {
        for (Node n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }

    public int calc() {
        if (children.size() == 1) return ((MulExp) children.get(0)).calc();
        else {
            int a = ((AddExp) children.get(0)).calc();
            int b = ((MulExp) children.get(2)).calc();
            if (((TokenNode) children.get(1)).getTokenType() == tokenType.PLUS) return a + b;
            else return a - b;
        }
    }

    @Override
    public Value genLLVMir() {
        if (children.size() == 1) return ((MulExp) children.get(0)).genLLVMir();
        else {
            Value operand1 = children.get(0).genLLVMir();
            Value operand2 = children.get(2).genLLVMir();
            BinaryInstr.op Op;
            if (operand1 instanceof ConstInteger constInteger && operand2 instanceof ConstInteger constInteger1) {
                switch (((TokenNode) children.get(1)).getTokenType()) {
                    case PLUS -> {
                        return new ConstInteger(constInteger.getVal() + constInteger1.getVal());
                    }
                    case MINU -> {
                        return new ConstInteger(constInteger.getVal() - constInteger1.getVal());
                    }
                    default -> {
                        return null;
                    }
                }
            } else if (operand2 instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                return operand1;
            }
            switch (((TokenNode) children.get(1)).getTokenType()) {
                case PLUS -> {
                    Op = BinaryInstr.op.ADD;
                }
                case MINU -> {
                    Op = BinaryInstr.op.SUB;
                }
                default -> {
                    Op = null;
                }
            }
            BinaryInstr binaryInstr = new BinaryInstr(new Integer32Type(), operand1, operand2, Op);
            IRController.getInstance().addInstr(binaryInstr);
            return binaryInstr;
        }
    }
}
