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

public class MulExp extends SynTreeNode {
    public MulExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public int getDim() {
        for (SynTreeNode n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }

    public int calc() {
        if (children.size() == 1) return ((UnaryExp) children.get(0)).calc();
        else {
            int a = ((MulExp) children.get(0)).calc();
            int b = ((UnaryExp) children.get(2)).calc();
            switch (((TokenSynTreeNode) children.get(1)).getTokenType()) {
                case MULT -> {
                    return a * b;
                }
                case DIV -> {
                    return a / b;
                }
                case MOD -> {
                    return a % b;
                }
                default -> {
                    return -1;
                }
            }
        }
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (children.size() == 1) {
            return ((UnaryExp) children.get(0)).genLLVMir();
        } else {
            LLVM_Value operand1 = children.get(0).genLLVMir();
            LLVM_Value operand2 = children.get(2).genLLVMir();
            BinaryInstr.op Op;
            if (operand1 instanceof ConstInteger constInteger && operand2 instanceof ConstInteger constInteger1) {
                switch (((TokenSynTreeNode) children.get(1)).getTokenType()) {
                    case MULT -> {
                        return new ConstInteger(constInteger.getVal() * constInteger1.getVal());
                    }
                    case DIV -> {
                        return new ConstInteger(constInteger.getVal() / constInteger1.getVal());
                    }
                    case MOD -> {
                        return new ConstInteger(constInteger.getVal() % constInteger1.getVal());
                    }
                    default -> {
                        return null;
                    }
                }
            } else if (operand1 instanceof ConstInteger constInteger) {
                if (constInteger.getVal() == 1 && ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.MULT) {
                    return operand2;
                } else if (constInteger.getVal() == 0) {
                    return new ConstInteger(0);
                }
            } else if (operand2 instanceof ConstInteger constInteger) {
                if (constInteger.getVal() == 1 && ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.MULT) {
                    return operand1;
                } else if (constInteger.getVal() == 0 && ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.MULT) {
                    return new ConstInteger(0);
                } else if (constInteger.getVal() == 1 && ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.DIV) {
                    return operand1;
                } else if (constInteger.getVal() == 1 && ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.MOD) {
                    return new ConstInteger(0);
                }
            } else if (operand1 == operand2) {
                if (((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.DIV) {
                    return new ConstInteger(1);
                } else if (((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.MOD) {
                    return new ConstInteger(0);
                }
            }
            switch (((TokenSynTreeNode) children.get(1)).getTokenType()) {
                case MULT -> {
                    Op = BinaryInstr.op.MUL;
                }
                case DIV -> {
                    Op = BinaryInstr.op.SDIV;
                }
                case MOD -> {
                    Op = BinaryInstr.op.SREM;
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
