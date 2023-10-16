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
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;

public class MainFuncDef extends Node {
    public MainFuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void checkError() {
        try {
            SymbolManager.getInstance().enterFuncBlock(new FuncSymbol("main", SymbolType.SYMBOL_FUNC, FunctionType.FUNC_INT, new ArrayList<>()));
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(1).getEndLine(), ErrorType.b));
        }
        Block block = (Block) children.get(children.size() - 1);
        if (!block.isLastStmtReturnInt())
            ErrorChecker.AddError(new Error(block.getEndLine(), ErrorType.g));
        super.checkError();
        SymbolManager.getInstance().leaveBlock();
    }
}
