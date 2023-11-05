package FrontEnd.Nodes.Var;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.Initial;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class ConstDef extends Node {

    private int dim = 0;

    private ArrayList<Integer> initValue;

    private String name;

    public ConstDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
        for (Node child : children) {
            if (child instanceof ConstExp) {
                dim++;
            }
        }
    }

    public String getName() {
        return name;
    }

    private VarSymbol createSymbol() {
        List<Integer> lens = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof ConstExp) {
                lens.add(((ConstExp) child).calc());
            }
        }
        Initial initValue = null;
        for (Node child : children) {
            if (child instanceof ConstInitVal && SymbolManager.getInstance().isGlobal()) {
                initValue = ((ConstInitVal) child).getVal();
            }
        }
        VarSymbol symbol = new VarSymbol(name, SymbolType.SYMBOL_CONST, dim, true, lens);
        if (initValue != null) {
            symbol.setInitValue(initValue);
        }
        return symbol;
    }

    @Override
    public Value genLLVMir() {
        VarSymbol symbol = createSymbol();
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        if (SymbolManager.getInstance().isGlobal()) {
            GlobalVar globalVar = new GlobalVar(symbol);
            IRController.getInstance().addGlobalVar(globalVar);
            //此时应当将生成GlobalVar
        } else {

        }
        return null;
    }

    @Override
    public void checkError() {
        Symbol symbol = createSymbol();
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        super.checkError();
    }
}
