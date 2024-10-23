package FrontEnd.AbsSynTreeNodes.Func;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Exp.ConstExp;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import FrontEnd.SymbolTable.Symbols.Symbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.Instr.AllocaInst;
import Ir_LLVM.LLVM_Values.Instr.StoreInstr;
import Ir_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;

public class FuncFParam extends Node {

    public FuncFParam(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public String getName() {
        return ((TokenNode) children.get(1)).getIdentName();
    }

    public int getDim() {
        int dim = 0;
        if (children.size() == 2) return dim;
        if (children.get(2) instanceof TokenNode && ((TokenNode) children.get(2)).getTokenType() == tokenType.LBRACK)
            dim++;
        for (Node n : children) if (n instanceof ConstExp) dim++;
        return dim;
    }

    @Override
    public void checkError() {
        try {
            SymbolTableBuilder.getInstance().addSymbol(new VarSymbol(getName(), SymbolType.SYMBOL_VAR, getDim(), false));
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        super.checkError();
    }

    @Override
    public LLVM_Value genLLVMir() {
        try {
            int width = 0;
            for (Node n : children) {
                if (n instanceof ConstExp) width = ((ConstExp) n).calc();
            }
            VarSymbol symbol = new VarSymbol(getName(), SymbolType.SYMBOL_VAR, getDim(), false, width);
            SymbolTableBuilder.getInstance().addSymbol(symbol);
            symbol.setAsParam();
            LLVM_Builder.getInstance().addParam(symbol);
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        return null;
    }

    public void setParamLLVMForFunc() {
        Symbol symbol = SymbolTableBuilder.getInstance().getSymbolByName(getName());
        LLVMType type1 = ((VarSymbol) symbol).getLLVMirValue().getType();
        AllocaInst allocaInst = new AllocaInst(type1);
        LLVM_Builder.getInstance().addInstr(allocaInst);
        StoreInstr storeInstr = new StoreInstr( symbol.getLLVMirValue(), allocaInst);
        LLVM_Builder.getInstance().addInstr(storeInstr);
        symbol.setLlvmValue(allocaInst);
    }
}
