package FrontEnd.AbsSynTreeNodes.Func;

import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.Parser.FunctionType;
import FrontEnd.SymbolTable.SymbolType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Block;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.ReturnInstr;
import IR_LLVM.LLVM_Types.Integer32Type;
import IR_LLVM.LLVM_Types.LLVMType;
import IR_LLVM.LLVM_Types.VoidType;

import java.util.ArrayList;

public class FuncDef extends SynTreeNode {

    private String funcName;

    private FunctionType functionType;

    private final ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public FuncDef(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        //TODO: fill the funcFParams
        funcName = ((TokenSynTreeNode) (children.get(1))).getIdentName();
        functionType = ((FrontEnd.AbsSynTreeNodes.Func.FuncType) children.get(0)).getValue().equals("void") ? FunctionType.FUNC_VOID : FunctionType.FUNC_INT;
        for (SynTreeNode child : children) {
            if (child instanceof FuncFParams) {
                funcFParams.addAll(((FuncFParams) child).getParamList());
            }
        }
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block,push this funcDef into symbolTable, insert the funcFParams
        ArrayList<Integer> list = new ArrayList<>();
        boolean isleave = true;
        for (FuncFParam f : funcFParams) list.add(f.getDim());
        try {
            SymbolTableBuilder.getInstance().enterFuncBlock(new FuncSymbol(funcName, SymbolType.SYMBOL_FUNC, functionType, list));
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
            isleave = false;
        }
        Block block = (Block) children.get(children.size() - 1);
        if (functionType == FunctionType.FUNC_INT) {
            if (!block.isLastStmtReturnInt())
                ErrorList.AddError(new Error(block.getEndLine(), ErrorType.g));
        } else {
            if (block.isReturnIntInFunc()) {
                ArrayList<Integer> list1 = block.getReturnIntLine();
                for (Integer i : list1) ErrorList.AddError(new Error(i, ErrorType.f));
            }
        }
        for (SynTreeNode synTreeNode : children) {
            if (synTreeNode instanceof Block) ((Block) synTreeNode).checkErrorInFunc();
            else synTreeNode.checkError();
        }
        if (isleave)
            SymbolTableBuilder.getInstance().leaveBlock();
        //TODO:Leave the symbol table
    }

    @Override
    public LLVM_Value genLLVMir() {
        SymbolTableBuilder.getInstance().setGlobal(false);
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<LLVMType> types = new ArrayList<>();
        for (FuncFParam f : funcFParams) {
            list.add(f.getDim());
            types.add(new Integer32Type());
        }
        FuncSymbol symbol = new FuncSymbol(funcName, SymbolType.SYMBOL_FUNC, functionType, list, types);
        try {
            SymbolTableBuilder.getInstance().enterFuncBlock(symbol);
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        boolean hasParam = false;
        for (SynTreeNode n : children) {
            if (n instanceof FuncFParams) {
                hasParam = true;
            }
        }
        Function function = new Function(functionType == FunctionType.FUNC_INT ? new Integer32Type() : new VoidType(), funcName, hasParam);
        symbol.setLlvmValue(function);
        LLVM_Builder.getInstance().addFunction(function);
        for (SynTreeNode n : children) {
            if (n instanceof FuncFParams) n.genLLVMir();
        }
        LLVM_Builder.getInstance().addNewBasicBlock(new BasicBlock());
        for (SynTreeNode n : children) {
            if (n instanceof FuncFParams) ((FuncFParams) n).setParamLLVMForFunc();
        }
        for (SynTreeNode n : children) {
            if (!(n instanceof FuncFParams)) n.genLLVMir();
        }
        if (function.getType() instanceof VoidType && !function.isLastInstrReturnVoid()) {
            ReturnInstr returnInstr = new ReturnInstr(new VoidType());
            LLVM_Builder.getInstance().addInstr(returnInstr);
        }
        SymbolTableBuilder.getInstance().leaveBlock();
        SymbolTableBuilder.getInstance().setGlobal(true);
        return null;
    }
}
