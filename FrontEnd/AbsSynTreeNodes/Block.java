package FrontEnd.AbsSynTreeNodes;

import FrontEnd.Parser.FunctionType;
import FrontEnd.AbsSynTreeNodes.Stmt.Stmt;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Value;

import java.util.ArrayList;

public class Block extends Stmt {
    public Block(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolTableBuilder.getInstance().enterBlock();
        super.checkError();
        SymbolTableBuilder.getInstance().leaveBlock();
    }

    public void checkErrorInFunc() {
        super.checkError();
    }

    public boolean isReturnIntInFunc() {
        for (SynTreeNode n : children) {
            if (n instanceof BlockItem && ((BlockItem) n).getReturnType() == FunctionType.FUNC_INT) return true;
        }
        return false;
    }

    public ArrayList<Integer> getReturnIntLine() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (SynTreeNode n : children) {
            if (n instanceof BlockItem && ((BlockItem) n).getReturnType() == FunctionType.FUNC_INT) ret.add(n.getStartLine());
        }
        return ret;
    }

    public boolean isLastStmtReturnInt() {
        if (children.get(children.size() - 2) instanceof BlockItem)
            return ((BlockItem) children.get(children.size() - 2)).getReturnType() == FunctionType.FUNC_INT;
        return false;
    }
    @Override
    public LLVM_Value genLLVMir() {
        SymbolTableBuilder.getInstance().enterBlock();
        super.genLLVMir();
        SymbolTableBuilder.getInstance().leaveBlock();
        return null;
    }
}
