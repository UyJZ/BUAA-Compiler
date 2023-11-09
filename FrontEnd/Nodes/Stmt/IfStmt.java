package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Cond;
import FrontEnd.Nodes.Node;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    public IfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value genLLVMir() {
        boolean hasElseBranch = children.size() > 5;
        BasicBlock Stmt1, Stmt2, nextBlock;
        if (hasElseBranch) {
            Stmt1 = new BasicBlock();
            Stmt2 = new BasicBlock();
            nextBlock = new BasicBlock();
            ((Cond) children.get(2)).setBlock(Stmt1, Stmt2);
            children.get(2).genLLVMir();
            IRController.getInstance().addNewBasicBlock(Stmt1);
            ((Stmt) children.get(4)).genLLVMir();
            BranchInstr branchInstr = new BranchInstr(new LLVMType(), nextBlock);
            IRController.getInstance().addInstr(branchInstr);
            IRController.getInstance().addNewBasicBlock(Stmt2);
            ((Stmt) children.get(6)).genLLVMir();
            BranchInstr branchInstr1 = new BranchInstr(new LLVMType(), nextBlock);
            IRController.getInstance().addInstr(branchInstr1);
            IRController.getInstance().addNewBasicBlock(nextBlock);
        } else {
            Stmt1 = new BasicBlock();
            nextBlock = new BasicBlock();
            ((Cond) children.get(2)).setBlock(Stmt1, nextBlock);
            children.get(2).genLLVMir();
            IRController.getInstance().addNewBasicBlock(Stmt1);
            children.get(4).genLLVMir();
            BranchInstr branchInstr = new BranchInstr(new LLVMType(), nextBlock);
            IRController.getInstance().addInstr(branchInstr);
            IRController.getInstance().addNewBasicBlock(nextBlock);
        }
        return null;
    }
}
