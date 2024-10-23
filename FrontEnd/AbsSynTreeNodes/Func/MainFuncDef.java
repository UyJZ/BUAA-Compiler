package FrontEnd.AbsSynTreeNodes.Func;

import Enums.ErrorType;
import Enums.FunctionType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Block;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Function;
import Ir_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class MainFuncDef extends Node {
    public MainFuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void checkError() {
        boolean isleave = true;
        try {
            SymbolTableBuilder.getInstance().enterFuncBlock(new FuncSymbol("main", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_INT, new ArrayList<>()));
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
            isleave = false;
        }
        Block block = (Block) children.get(children.size() - 1);
        if (!block.isLastStmtReturnInt())
            ErrorList.AddError(new Error(block.getEndLine(), ErrorType.g));
        super.checkError();
        if (isleave)
            SymbolTableBuilder.getInstance().leaveBlock();
    }

    @Override
    public LLVM_Value genLLVMir() {
        SymbolTableBuilder.getInstance().setGlobal(false);
        try {
            SymbolTableBuilder.getInstance().enterFuncBlock(new FuncSymbol("main", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_INT, new ArrayList<>()));
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        Function function = new Function(new Integer32Type(), "main", false);
        LLVM_Builder.getInstance().addFunction(function);
        LLVM_Builder.getInstance().addNewBasicBlock(new BasicBlock());
        super.genLLVMir();
        return null;
    }
}
