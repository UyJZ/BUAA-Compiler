package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public BreakStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        if (SymbolManager.getInstance().getLoopDepth() == 0)
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.m));
    }

    @Override
    public Value genLLVMir() {
        BranchInstr branchInstr = new BranchInstr(new LLVMType(), this.NextBlock);
        IRController.getInstance().addInstr(branchInstr);
        IRController.getInstance().addNewBasicBlock(new BasicBlock());
        return null;
    }
}
