package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.Parser.FunctionType;
import FrontEnd.SymbolTable.SymbolType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Block;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;

public class MainFuncDef extends SynTreeNode {
    public MainFuncDef(SyntaxVarType type, ArrayList<SynTreeNode> children) {
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
