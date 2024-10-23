package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.BinaryInstr;
import Ir_LLVM.LLVM_Types.Integer32Type;

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
    public LLVM_Value genLLVMir() {
        if (children.size() == 1) return ((MulExp) children.get(0)).genLLVMir();
        else {
            LLVM_Value operand1 = children.get(0).genLLVMir();
            LLVM_Value operand2 = children.get(2).genLLVMir();
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
            LLVM_Builder.getInstance().addInstr(binaryInstr);
            return binaryInstr;
        }
    }
}
