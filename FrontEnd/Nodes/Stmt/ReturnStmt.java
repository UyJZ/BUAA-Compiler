package FrontEnd.Nodes.Stmt;

import Enums.FunctionType;
import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.Exp.Exp;
import llvm_ir.IRController;
import llvm_ir.Value;
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
        if (children.size() == 1) return new ReturnInstr(new VoidType(), "void");
        else if (children.size() == 3) {
            Value operand = ((Exp) children.get(1)).genLLVMir();
            ReturnInstr ret = new ReturnInstr(new Integer32Type(), operand.getName());
            IRController.getInstance().addInstr(ret);
            return ret;
        } else {
            ReturnInstr ret = new ReturnInstr(new VoidType(), "");
            IRController.getInstance().addInstr(ret);
            return ret;
        }
    }

}
