package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public BreakStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        if (SymbolTableBuilder.getInstance().getLoopDepth() == 0)
            ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.m));
    }

    @Override
    public LLVM_Value genLLVMir() {
        BranchInstr branchInstr = new BranchInstr(this.NextBlock);
        LLVM_Builder.getInstance().addInstr(branchInstr);
        LLVM_Builder.getInstance().addNewBasicBlock(new BasicBlock());
        return null;
    }
}