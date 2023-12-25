package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;
import MyPackage.Symbol.SymbolTable;

public class ForStmt {
    private LVal lVal;
    private Exp exp;

    public ForStmt(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }

    public void generateLlvm() {
        SymbolTable symbolTable =  IRModule.getCurTable();
        Symbol symbol = symbolTable.search(lVal.getIdent());
        if (!(symbol instanceof MyValSymbol) || ((MyValSymbol) symbol).getReg() == null) {
            symbolTable = symbolTable.getPre();
            symbol = symbolTable.search(lVal.getIdent());
        }
        Value value = exp.generateLlvm();
        StoreLlvm storeLlvm = new StoreLlvm(value.getType(), value.getValue(), (MyValSymbol)symbol);
        Value value1 = lVal.generateLlvm((MyValSymbol)symbol);
        storeLlvm.addOperand(value);
        if (value1 == null) {
            storeLlvm.addOperand(((MyValSymbol) symbol).getReg());
        }
        else {
            storeLlvm.addOperand(value1);
        }
        IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
    }

}
