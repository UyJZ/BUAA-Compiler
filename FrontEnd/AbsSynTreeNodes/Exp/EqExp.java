package FrontEnd.AbsSynTreeNodes.Exp;

import FrontEnd.Lexer.Token;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.ConstBool;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Instr.IcmpInstr;
import IR_LLVM.LLVM_Values.Instr.ZextInstr;
import IR_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class EqExp extends SynTreeNode {

    private BasicBlock newBasicBlock, trueBlock, falseBlock;

    public EqExp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock newBasicBlock, BasicBlock trueBlock, BasicBlock falseBlock) {
        this.newBasicBlock = newBasicBlock;
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (newBasicBlock != null)
            LLVM_Builder.getInstance().addNewBasicBlock(newBasicBlock);
        if (children.size() == 1) {
            return children.get(0).genLLVMir();
        } else {
            LLVM_Value left = children.get(0).genLLVMir();
            IcmpInstr.CmpOp cmpOp = ((TokenSynTreeNode) children.get(1)).getTokenType() == Token.TokenType.EQL ? IcmpInstr.CmpOp.eq : IcmpInstr.CmpOp.ne;
            LLVM_Value right = children.get(2).genLLVMir();
            if (left instanceof ConstBool constBool && right instanceof ConstBool constBool1) {
                boolean ans = false;
                if (cmpOp == IcmpInstr.CmpOp.eq) return new ConstBool(constBool.isTrue() == constBool1.isTrue());
                else return new ConstBool(constBool.isTrue() != constBool1.isTrue());
            }
            if (!left.getType().equals(right.getType())) {
                if (left instanceof ConstBool constBool) {
                    IcmpInstr icmpInstr = new IcmpInstr(new ConstInteger(constBool.getVal()), right, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else if (right instanceof ConstBool constBool) {
                    IcmpInstr icmpInstr = new IcmpInstr(left, new ConstInteger(constBool.getVal()), cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else if (left.getType() instanceof Integer32Type) {
                    ZextInstr zextInstr = new ZextInstr(right.getType(), left.getType(), right);
                    LLVM_Builder.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(left, zextInstr, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else {
                    ZextInstr zextInstr = new ZextInstr(left.getType(), right.getType(), left);
                    LLVM_Builder.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(zextInstr, right, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                }
            }
            return Compare(left, right, cmpOp);
        }
    }

    private LLVM_Value Compare(LLVM_Value left, LLVM_Value right, IcmpInstr.CmpOp cmpOp) {
        IcmpInstr icmpInstr = new IcmpInstr(left, right, cmpOp);
        LLVM_Builder.getInstance().addInstr(icmpInstr);
        return icmpInstr;
    }
}
