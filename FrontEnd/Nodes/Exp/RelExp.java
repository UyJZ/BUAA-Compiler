package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.Values.Instruction.ZextInstr;
import llvm_ir.llvmType.Integer32Type;

import java.util.ArrayList;

public class RelExp extends Node {
    public RelExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value genLLVMir() {
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
            Value left = children.get(0).genLLVMir();
            Value right = children.get(2).genLLVMir();
            if (!left.getType().equals(right.getType())) {
                ZextInstr zextInstr;
                if (right.getType() instanceof Integer32Type) {
                    zextInstr = new ZextInstr(left.getType(), right.getType(), left.getName());
                    IRController.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(right.getType(), zextInstr.getName(), right.getName(), cmpOp);
                    IRController.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else {
                    zextInstr = new ZextInstr(right.getType(), left.getType(), right.getName());
                    IRController.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(zextInstr.getType(), left.getName(), zextInstr.getName(), cmpOp);
                    IRController.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                }
            } else {
                IcmpInstr icmpInstr = new IcmpInstr(left.getType(), left.getName(), right.getName(), cmpOp);
                IRController.getInstance().addInstr(icmpInstr);
                return icmpInstr;
            }
        }
    }
}
