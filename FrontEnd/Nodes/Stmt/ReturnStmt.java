package FrontEnd.Nodes.Stmt;

import Enums.FunctionType;
import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.Exp.Exp;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;

public class ReturnStmt extends Stmt {
    public ReturnStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FunctionType getReturnType() {
        for (Node n : children) {
            if (n instanceof Exp) {
                return FunctionType.FUNC_INT;
            }
        }
        return FunctionType.FUNC_VOID;
    }

    @Override
    public Value genLLVMir() {
        ReturnInstr ret;
        if (children.size() == 1) {
            ret = new ReturnInstr(new VoidType(), "void");
            IRController.getInstance().addInstr(ret);
        } else if (children.size() == 3) {
            Value operand = ((Exp) children.get(1)).genLLVMir();
            ret = new ReturnInstr(new Integer32Type(), operand);
            IRController.getInstance().addInstr(ret);
        } else {
            ret = new ReturnInstr(new VoidType(), "");
            IRController.getInstance().addInstr(ret);
        }
        BasicBlock block = new BasicBlock();
        IRController.getInstance().addNewBasicBlock(block);
        return ret;
    }

}
