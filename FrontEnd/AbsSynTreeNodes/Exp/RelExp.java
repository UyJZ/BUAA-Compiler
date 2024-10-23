package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.ConstBool;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.IcmpInstr;
import Ir_LLVM.LLVM_Values.Instr.ZextInstr;
import Ir_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class RelExp extends Node {
    public RelExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (children.size() == 1) {
            return children.get(0).genLLVMir();
        } else {
            IcmpInstr.CmpOp cmpOp;
            switch (((TokenNode) children.get(1)).getTokenType()) {
                case LSS -> {
                    cmpOp = IcmpInstr.CmpOp.slt;
                }
                case LEQ -> {
                    cmpOp = IcmpInstr.CmpOp.sle;
                }
                case GRE -> {
                    cmpOp = IcmpInstr.CmpOp.sgt;
                }
                case GEQ -> {
                    cmpOp = IcmpInstr.CmpOp.sge;
                }
                default -> {
                    cmpOp = null;
                }
            }
            LLVM_Value left = children.get(0).genLLVMir();
            LLVM_Value right = children.get(2).genLLVMir();
            if (left instanceof ConstBool constBool && right instanceof ConstBool constBool1) {
                if (cmpOp == IcmpInstr.CmpOp.slt) return new ConstBool(constBool.getVal() < constBool1.getVal());
                else if (cmpOp == IcmpInstr.CmpOp.sle) return new ConstBool(constBool.getVal() <= constBool1.getVal());
                else if (cmpOp == IcmpInstr.CmpOp.sgt) return new ConstBool(constBool.getVal() > constBool1.getVal());
                else if (cmpOp == IcmpInstr.CmpOp.sge) return new ConstBool(constBool.getVal() >= constBool1.getVal());
            }
            if (!left.getType().equals(right.getType())) {
                ZextInstr zextInstr;
                if (left instanceof ConstBool constBool) {
                    IcmpInstr icmpInstr = new IcmpInstr(new ConstInteger(constBool.getVal()), right, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else if (right instanceof ConstBool constBool) {
                    IcmpInstr icmpInstr = new IcmpInstr(left, new ConstInteger(constBool.getVal()), cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else if (right.getType() instanceof Integer32Type) {
                    zextInstr = new ZextInstr(left.getType(), right.getType(), left);
                    LLVM_Builder.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(zextInstr, right, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else {
                    zextInstr = new ZextInstr(right.getType(), left.getType(), right);
                    LLVM_Builder.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(left, zextInstr, cmpOp);
                    LLVM_Builder.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                }
            } else {
                IcmpInstr icmpInstr = new IcmpInstr(left, right, cmpOp);
                LLVM_Builder.getInstance().addInstr(icmpInstr);
                return icmpInstr;
            }
        }
    }
}
