package FrontEnd.AbsSynTreeNodes;

import FrontEnd.Parser.FunctionType;
import FrontEnd.SymbolTable.SymbolType;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Types.*;

import java.util.ArrayList;

public class CompUnit extends SynTreeNode {
    public CompUnit(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block
        SymbolTableBuilder.getInstance().enterBlock();
        super.checkError();
        SymbolTableBuilder.getInstance().leaveBlock();
        //TODO:Leave the symbol table
    }

    @Override
    public LLVM_Value genLLVMir() {
        SymbolTableBuilder.getInstance().enterBlock();
        FuncSymbol getint = new FuncSymbol("getint", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_INT, new ArrayList<>());
        try {
            SymbolTableBuilder.getInstance().addSysSymbol(getint);
        } catch (RenameException e) {
        }
        getint.setLlvmValue(new Function(new Integer32Type(), "getint", false));
        ArrayList<Integer> dims = new ArrayList<>();
        ArrayList<LLVMType> types = new ArrayList<>();
        dims.add(1);
        types.add(new Integer32Type());
        FuncSymbol putint = new FuncSymbol("putint", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims, types);
        try {
            SymbolTableBuilder.getInstance().addSysSymbol(putint);
        } catch (RenameException e) {
        }
        putint.setLlvmValue(new Function(new VoidType(), "putint", true));
        ArrayList<Integer> dims1 = new ArrayList<>();
        ArrayList<LLVMType> types1 = new ArrayList<>();
        dims1.add(1);
        types1.add(new Integer32Type());
        FuncSymbol putch = new FuncSymbol("putch", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims1, types1);
        try {
            SymbolTableBuilder.getInstance().addSysSymbol(putch);
        } catch (RenameException e) {
        }
        putch.setLlvmValue(new Function(new VoidType(), "putch", true));
        ArrayList<Integer> dims2 = new ArrayList<>();
        ArrayList<LLVMType> types2 = new ArrayList<>();
        dims2.add(1);
        types2.add(new PointerType(new Integer8Type()));
        FuncSymbol putstr = new FuncSymbol("putstr", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims2, types2);
        try {
            SymbolTableBuilder.getInstance().addSysSymbol(putstr);
        } catch (RenameException e) {
        }
        putstr.setLlvmValue(new Function(new VoidType(), "putstr", true));
        super.genLLVMir();
        SymbolTableBuilder.getInstance().leaveBlock();
        return null;
    }
}
