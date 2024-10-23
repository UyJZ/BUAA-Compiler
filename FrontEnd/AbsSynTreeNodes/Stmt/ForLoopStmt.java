package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Cond;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;

import java.util.ArrayList;

public class ForLoopStmt extends Stmt {
    public ForLoopStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof ForStmt && i == 2) {
                forStmt1 = (ForStmt) children.get(i);
            } else if (children.get(i) instanceof ForStmt) {
                forStmt2 = (ForStmt) children.get(i);
            } else if (children.get(i) instanceof Cond) {
                cond = (Cond) children.get(i);
            } else if (children.get(i) instanceof Stmt && !(children.get(i) instanceof ForStmt)) {
                stmt = (Stmt) children.get(i);
            }
        }
    }

    private ForStmt forStmt1, forStmt2;

    private Cond cond;

    private Stmt stmt;

    @Override
    public void checkError() {
        SymbolTableBuilder.getInstance().enterLoopBlock();
        super.checkError();
        SymbolTableBuilder.getInstance().leaveLoopBlock();
    }

    @Override
    public LLVM_Value genLLVMir() {
        SymbolTableBuilder.getInstance().enterLoopBlock();
        BasicBlock Cond = new BasicBlock(), ForStmt2 = new BasicBlock(), nextBlock = new BasicBlock();
        BasicBlock Stmt = new BasicBlock();
        if (forStmt1 != null) {
            forStmt1.genLLVMir();
        }
        BranchInstr branchInstr0 = new BranchInstr(Cond);
        LLVM_Builder.getInstance().addInstr(branchInstr0);
        LLVM_Builder.getInstance().addNewBasicBlock(Cond);
        if (cond != null) {
            cond.setBlock(Stmt, nextBlock);
            cond.genLLVMir();
        } else {
            BranchInstr branchInstr = new BranchInstr(Stmt);
            LLVM_Builder.getInstance().addInstr(branchInstr);
        }
        LLVM_Builder.getInstance().addNewBasicBlock(Stmt);
        stmt.setBlockForLoop(ForStmt2, nextBlock);
        stmt.genLLVMir();
        BranchInstr branchInstr = new BranchInstr( ForStmt2);
        LLVM_Builder.getInstance().addInstr(branchInstr);
        LLVM_Builder.getInstance().addNewBasicBlock(ForStmt2);
        if (forStmt2 != null) {
            forStmt2.genLLVMir();
        }
        BranchInstr branchInstr1 = new BranchInstr(Cond);
        LLVM_Builder.getInstance().addInstr(branchInstr1);
        LLVM_Builder.getInstance().addNewBasicBlock(nextBlock);
        SymbolTableBuilder.getInstance().leaveLoopBlock();
        return null;
    }
}
