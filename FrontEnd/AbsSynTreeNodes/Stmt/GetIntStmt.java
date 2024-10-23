package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.Symbols.Symbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.Instr.CallInstr;
import IR_LLVM.LLVM_Values.Instr.StoreInstr;
import IR_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class GetIntStmt extends Stmt {

    private LVal lVal;

    public GetIntStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        for (SynTreeNode synTreeNode : children) if (synTreeNode instanceof LVal) lVal = (LVal) synTreeNode;
    }

    @Override
    public void checkError() {
        VarSymbol varSymbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        if (varSymbol.isConst()) ErrorList.AddError(new Error(lVal.identLine(), ErrorType.h));
        super.checkError();
    }

    @Override
    public LLVM_Value genLLVMir() {
        Symbol symbol = SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        FuncSymbol funcSymbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName("getint");
        CallInstr callInstr = new CallInstr(new Integer32Type(), funcSymbol.getLLVMirValue(), new ArrayList<>());
        LLVM_Builder.getInstance().addInstr(callInstr);
        LLVM_Value operand1 = lVal.genLLVMForAssign();
        StoreInstr instr = new StoreInstr(callInstr, operand1);
        LLVM_Builder.getInstance().addInstr(instr);
        return instr;
    }
}
