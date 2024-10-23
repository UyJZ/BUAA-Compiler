package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.Instr.StoreInstr;

import java.util.ArrayList;

public class ForStmt extends SynTreeNode {

    private LVal lVal;

    private Exp expr;

    public ForStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public LLVM_Value genLLVMir() {
        LLVM_Value operand = children.get(2).genLLVMir();
        for (SynTreeNode synTreeNode : children) {
            if (synTreeNode instanceof LVal) lVal = (LVal) synTreeNode;
            else if (synTreeNode instanceof Exp) expr = (Exp) synTreeNode;
        }
        VarSymbol symbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        StoreInstr instr = new StoreInstr(operand, symbol.getLLVMirValue());
        LLVM_Builder.getInstance().addInstr(instr);
        return instr;
    }
}
