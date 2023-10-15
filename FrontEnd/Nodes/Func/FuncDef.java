package FrontEnd.Nodes.Func;

import Enums.ErrorType;
import Enums.FuncType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Block;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;

import java.util.ArrayList;

public class FuncDef extends Node {

    private String funcName;

    private FuncType funcType;

    private final ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        //TODO: fill the funcFParams
        funcName = (children.get(1)).toString();
        funcType = children.get(0).toString().equals("void") ? FuncType.FUNC_VOID : FuncType.FUNC_INT;
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
            SymbolManager.getInstance().enterFuncBlock(new FuncSymbol(funcName, SymbolType.SYMBOL_FUNC, funcType, list));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        Block block = (Block) children.get(children.size() - 1);
        if (funcType == FuncType.FUNC_INT) {
            if (!block.isLastStmtReturnInt())
                ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.g));
        } else {
            if (block.isReturnIntInFunc())
                ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.f));
        }
        for (Node node : children) {
            if (node instanceof Block) ((Block) node).checkErrorInFunc();
            else node.checkError();
        }
        SymbolManager.getInstance().leaveBlock();
        //TODO:Leave the symbol table
    }
}
