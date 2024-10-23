package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import FrontEnd.Lexer.Token;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Instr.BinaryInstr;
import IR_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class AddExp extends SynTreeNode {
    public AddExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public int getDim() {
        for (SynTreeNode n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }

    public int calc() {
        if (children.size() == 1) return ((MulExp) children.get(0)).calc();
        else {
            int a = ((AddExp) children.get(0)).calc();
            int b = ((MulExp) children.get(2)).calc();
            if (((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.PLUS) return a + b;
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
                switch (((TokenSynTreeNode) children.get(1)).getTokenType()) {
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
            switch (((TokenSynTreeNode) children.get(1)).getTokenType()) {
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
