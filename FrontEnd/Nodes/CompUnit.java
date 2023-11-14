package FrontEnd.Nodes;

import Enums.FunctionType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Function;
import llvm_ir.llvmType.*;

import java.util.ArrayList;

public class CompUnit extends Node {
    public CompUnit(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block
        SymbolManager.getInstance().enterBlock();
        super.checkError();
        SymbolManager.getInstance().leaveBlock();
        //TODO:Leave the symbol table
    }

    @Override
    public Value genLLVMir() {
        SymbolManager.getInstance().enterBlock();
        FuncSymbol getint = new FuncSymbol("getint", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_INT, new ArrayList<>());
        try {
            SymbolManager.getInstance().addSymbol(getint);
        } catch (RenameException e) {
        }
        getint.setLlvmValue(new Function(new Integer32Type(), "getint", false));
        ArrayList<Integer> dims = new ArrayList<>();
        ArrayList<LLVMType> types = new ArrayList<>();
        dims.add(1);
        types.add(new Integer32Type());
        FuncSymbol putint = new FuncSymbol("putint", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims, types);
        try {
            SymbolManager.getInstance().addSymbol(putint);
        } catch (RenameException e) {
        }
        putint.setLlvmValue(new Function(new VoidType(), "putint", true));
        ArrayList<Integer> dims1 = new ArrayList<>();
        ArrayList<LLVMType> types1 = new ArrayList<>();
        dims1.add(1);
        types1.add(new Integer32Type());
        FuncSymbol putch = new FuncSymbol("putch", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims1, types1);
        try {
            SymbolManager.getInstance().addSymbol(putch);
        } catch (RenameException e) {
        }
        putch.setLlvmValue(new Function(new VoidType(), "putch", true));
        ArrayList<Integer> dims2 = new ArrayList<>();
        ArrayList<LLVMType> types2 = new ArrayList<>();
        dims2.add(1);
        types2.add(new PointerType(new Integer8Type()));
        FuncSymbol putstr = new FuncSymbol("putstr", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_VOID, dims2, types2);
        try {
            SymbolManager.getInstance().addSymbol(putstr);
        } catch (RenameException e) {
        }
        putstr.setLlvmValue(new Function(new VoidType(), "putstr", true));
        super.genLLVMir();
        SymbolManager.getInstance().leaveBlock();
        return null;
    }
}
