package FrontEnd.Nodes.Func;

import Enums.ErrorType;
import Enums.FunctionType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Block;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;

public class FuncDef extends Node {

    private String funcName;

    private FunctionType functionType;

    private final ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        //TODO: fill the funcFParams
        funcName = ((TokenNode) (children.get(1))).getIdentName();
        functionType = ((FrontEnd.Nodes.Func.FuncType) children.get(0)).getValue().equals("void") ? FunctionType.FUNC_VOID : FunctionType.FUNC_INT;
        for (Node child : children) {
            if (child instanceof FuncFParams) {
                funcFParams.addAll(((FuncFParams) child).getParamList());
            }
        }
    }

    @Override
    public void checkError() {
        //TODO:Enter the symbol table and create a new block,push this funcDef into symbolTable, insert the funcFParams
        ArrayList<Integer> list = new ArrayList<>();
        for (FuncFParam f : funcFParams) list.add(f.getDim());
        try {
            SymbolManager.getInstance().enterFuncBlock(new FuncSymbol(funcName, SymbolType.SYMBOL_FUNC, functionType, list));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        Block block = (Block) children.get(children.size() - 1);
        if (functionType == FunctionType.FUNC_INT) {
            if (!block.isLastStmtReturnInt())
                ErrorChecker.AddError(new Error(block.getEndLine(), ErrorType.g));
        } else {
            if (block.isReturnIntInFunc()) {
                ArrayList<Integer> list1 = block.getReturnIntLine();
                for (Integer i : list1) ErrorChecker.AddError(new Error(i, ErrorType.f));
            }
        }
        for (Node node : children) {
            if (node instanceof Block) ((Block) node).checkErrorInFunc();
            else node.checkError();
        }
        SymbolManager.getInstance().leaveBlock();
        //TODO:Leave the symbol table
    }

    @Override
    public Value genLLVMir() {
        SymbolManager.getInstance().setGlobal(false);
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<LLVMType> types = new ArrayList<>();
        for (FuncFParam f : funcFParams) {
            list.add(f.getDim());
            types.add(new Integer32Type());
        }
        try {
            SymbolManager.getInstance().enterFuncBlock(new FuncSymbol(funcName, SymbolType.SYMBOL_FUNC, functionType, list, types));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        boolean hasParam = false;
        for (Node n : children) {
            if (n instanceof FuncFParams) {
                hasParam = true;
            }
        }
        Function function = new Function(functionType == FunctionType.FUNC_INT ? new Integer32Type() : new VoidType(), funcName, hasParam);
        IRController.getInstance().addFunction(function);
        for (Node n : children) {
            if (n instanceof FuncFParams) n.genLLVMir();
        }
        IRController.getInstance().addNewBasicBlock(new BasicBlock(new BasicBlockType(), IRController.getInstance().genVirtualRegNum()));
        for (Node n : children) {
            if (n instanceof FuncFParams) ((FuncFParams) n).setParamLLVMForFunc();
        }
        for (Node n : children) {
            if (!(n instanceof FuncFParams)) n.genLLVMir();
        }
        SymbolManager.getInstance().leaveBlock();
        SymbolManager.getInstance().setGlobal(true);
        return null;
    }
}
