package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.Lexer.Token;
import FrontEnd.SymbolTable.SymbolType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Exp.ConstExp;
import FrontEnd.SymbolTable.Symbols.Symbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.Instr.AllocaInst;
import IR_LLVM.LLVM_Values.Instr.StoreInstr;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;

public class FuncFParam extends SynTreeNode {

    public FuncFParam(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public String getName() {
        return ((TokenSynTreeNode) children.get(1)).getIdentName();
    }

    public int getDim() {
        int dim = 0;
        if (children.size() == 2) return dim;
        if (children.get(2) instanceof TokenSynTreeNode && ((TokenSynTreeNode) children.get(2)).getTokenType() == Token.TokenType.LBRACK)
            dim++;
        for (SynTreeNode n : children) if (n instanceof ConstExp) dim++;
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
            for (SynTreeNode n : children) {
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
