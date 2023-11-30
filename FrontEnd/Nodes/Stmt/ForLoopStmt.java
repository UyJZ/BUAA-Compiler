package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Cond;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.llvmType.LLVMType;

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
        SymbolManager.getInstance().enterLoopBlock();
        super.checkError();
        SymbolManager.getInstance().leaveLoopBlock();
    }

    @Override
    public Value genLLVMir() {
        SymbolManager.getInstance().enterLoopBlock();
        BasicBlock Cond = new BasicBlock(), ForStmt2 = new BasicBlock(), nextBlock = new BasicBlock();
        BasicBlock Stmt = new BasicBlock();
        if (forStmt1 != null) {
            forStmt1.genLLVMir();
        }
        BranchInstr branchInstr0 = new BranchInstr(Cond);
        IRController.getInstance().addInstr(branchInstr0);
        IRController.getInstance().addNewBasicBlock(Cond);
        if (cond != null) {
            cond.setBlock(Stmt, nextBlock);
            cond.genLLVMir();
        } else {
            BranchInstr branchInstr = new BranchInstr(Stmt);
            IRController.getInstance().addInstr(branchInstr);
        }
        IRController.getInstance().addNewBasicBlock(Stmt);
        stmt.setBlockForLoop(ForStmt2, nextBlock);
        stmt.genLLVMir();
        BranchInstr branchInstr = new BranchInstr( ForStmt2);
        IRController.getInstance().addInstr(branchInstr);
        IRController.getInstance().addNewBasicBlock(ForStmt2);
        if (forStmt2 != null) {
            forStmt2.genLLVMir();
        }
        BranchInstr branchInstr1 = new BranchInstr(Cond);
        IRController.getInstance().addInstr(branchInstr1);
        IRController.getInstance().addNewBasicBlock(nextBlock);
        SymbolManager.getInstance().leaveLoopBlock();
        return null;
    }
}
