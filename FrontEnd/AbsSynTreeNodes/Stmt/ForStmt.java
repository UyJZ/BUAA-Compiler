package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.Instr.StoreInstr;

import java.util.ArrayList;

public class ForStmt extends Node {

    private LVal lVal;

    private Exp expr;

    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public LLVM_Value genLLVMir() {
        LLVM_Value operand = children.get(2).genLLVMir();
        for (Node node : children) {
            if (node instanceof LVal) lVal = (LVal) node;
            else if (node instanceof Exp) expr = (Exp) node;
        }
        VarSymbol symbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        StoreInstr instr = new StoreInstr(operand, symbol.getLLVMirValue());
        LLVM_Builder.getInstance().addInstr(instr);
        return instr;
    }
}
