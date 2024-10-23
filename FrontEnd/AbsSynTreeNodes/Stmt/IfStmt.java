package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.Cond;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    public IfStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public LLVM_Value genLLVMir() {
        boolean hasElseBranch = children.size() > 5;
        BasicBlock Stmt1, Stmt2, nextBlock;
        if (hasElseBranch) {
            Stmt1 = new BasicBlock();
            Stmt2 = new BasicBlock();
            nextBlock = new BasicBlock();
            ((Cond) children.get(2)).setBlock(Stmt1, Stmt2);
            children.get(2).genLLVMir();
            LLVM_Builder.getInstance().addNewBasicBlock(Stmt1);
            ((Stmt) children.get(4)).genLLVMir();
            BranchInstr branchInstr = new BranchInstr(nextBlock);
            LLVM_Builder.getInstance().addInstr(branchInstr);
            LLVM_Builder.getInstance().addNewBasicBlock(Stmt2);
            ((Stmt) children.get(6)).genLLVMir();
            BranchInstr branchInstr1 = new BranchInstr(nextBlock);
            LLVM_Builder.getInstance().addInstr(branchInstr1);
            LLVM_Builder.getInstance().addNewBasicBlock(nextBlock);
        } else {
            Stmt1 = new BasicBlock();
            nextBlock = new BasicBlock();
            ((Cond) children.get(2)).setBlock(Stmt1, nextBlock);
            children.get(2).genLLVMir();
            LLVM_Builder.getInstance().addNewBasicBlock(Stmt1);
            children.get(4).genLLVMir();
            BranchInstr branchInstr = new BranchInstr(nextBlock);
            LLVM_Builder.getInstance().addInstr(branchInstr);
            LLVM_Builder.getInstance().addNewBasicBlock(nextBlock);
        }
        return null;
    }
}
