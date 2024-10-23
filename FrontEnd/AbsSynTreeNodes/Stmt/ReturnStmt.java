package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.FunctionType;
import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Instr.terminatorInstr.ReturnInstr;
import Ir_LLVM.LLVM_Types.Integer32Type;
import Ir_LLVM.LLVM_Types.VoidType;

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
    public LLVM_Value genLLVMir() {
        ReturnInstr ret;
        if (children.size() == 1) {
            ret = new ReturnInstr(new VoidType());
            LLVM_Builder.getInstance().addInstr(ret);
        } else if (children.size() == 3) {
            LLVM_Value operand = ((Exp) children.get(1)).genLLVMir();
            ret = new ReturnInstr(new Integer32Type(), operand);
            LLVM_Builder.getInstance().addInstr(ret);
        } else {
            ret = new ReturnInstr(new VoidType());
            LLVM_Builder.getInstance().addInstr(ret);
        }
        BasicBlock block = new BasicBlock();
        LLVM_Builder.getInstance().addNewBasicBlock(block);
        return ret;
    }

}
