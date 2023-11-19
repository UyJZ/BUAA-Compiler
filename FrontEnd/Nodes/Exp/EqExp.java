package FrontEnd.Nodes.Exp;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.Values.Instruction.ZextInstr;
import llvm_ir.llvmType.Integer32Type;

import java.util.ArrayList;

public class EqExp extends Node {

    private BasicBlock newBasicBlock, trueBlock, falseBlock;

    public EqExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void setBlock(BasicBlock newBasicBlock, BasicBlock trueBlock, BasicBlock falseBlock) {
        this.newBasicBlock = newBasicBlock;
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    @Override
    public Value genLLVMir() {
        if (newBasicBlock != null)
            IRController.getInstance().addNewBasicBlock(newBasicBlock);
        if (children.size() == 1) {
            return children.get(0).genLLVMir();
        } else {
            Value left = children.get(0).genLLVMir();
            IcmpInstr.CmpOp cmpOp = ((TokenNode) children.get(1)).getTokenType() == tokenType.EQL ? IcmpInstr.CmpOp.eq : IcmpInstr.CmpOp.ne;
            Value right = children.get(2).genLLVMir();
            if (!left.getType().equals(right.getType())) {
                if (left.getType() instanceof Integer32Type) {
                    ZextInstr zextInstr = new ZextInstr(right.getType(), left.getType(), right);
                    IRController.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(left, zextInstr, cmpOp);
                    IRController.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                } else {
                    ZextInstr zextInstr = new ZextInstr(left.getType(), right.getType(), left);
                    IRController.getInstance().addInstr(zextInstr);
                    IcmpInstr icmpInstr = new IcmpInstr(zextInstr, right, cmpOp);
                    IRController.getInstance().addInstr(icmpInstr);
                    return icmpInstr;
                }
            }
            return Compare(left, right, cmpOp);
        }
    }

    private Value Compare(Value left, Value right, IcmpInstr.CmpOp cmpOp) {
        IcmpInstr icmpInstr = new IcmpInstr(left, right, cmpOp);
        IRController.getInstance().addInstr(icmpInstr);
        return icmpInstr;
    }
}
