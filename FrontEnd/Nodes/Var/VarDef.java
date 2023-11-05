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
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;

public class VarDef extends Node {

    private int dim = 0;
    private final String name;

    private boolean isAssigned;

    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
        isAssigned = false;
        for (Node child : children) {
            if (child.toString().equals("=")) isAssigned = true;
            if (child instanceof ConstExp) {
                dim++;
            }
        }
    }

    private VarSymbol createSymbol() {
        ArrayList<Integer> lens = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof ConstExp) {
                lens.add(((ConstExp) child).calc());
            }
        }
        Initial initValue = null;
        for (Node child : children) {
            if (child instanceof InitVal && SymbolManager.getInstance().isGlobal()) {
                initValue = ((InitVal) child).getVal();
            }
        }
        VarSymbol symbol = new VarSymbol(name, SymbolType.SYMBOL_VAR, dim, false, lens);
        if (initValue != null) {
            symbol.setInitValue(initValue);
        }
        return symbol;
    }

    @Override
    public void checkError() {
        Symbol symbol = createSymbol();
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
    }

    @Override
    public Value genLLVMir() {
        VarSymbol symbol = createSymbol();
        if (SymbolManager.getInstance().isGlobal()) {
            GlobalVar globalVar = new GlobalVar(symbol);
            IRController.getInstance().addGlobalVar(globalVar);
        } else {
            AllocaInst allocaInst = new AllocaInst(new Integer32Type(), IRController.getInstance().genVirtualRegNum());
            IRController.getInstance().addInstr(allocaInst);
            for (Node n : children) {
                if (n instanceof InitVal) {
                    Value value = ((InitVal) n).genLLVMir();
                    StoreInstr storeInstr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), value.getName(), allocaInst.getName());
                    IRController.getInstance().addInstr(storeInstr);
                }
            }
            symbol.setLlvmValue(allocaInst);
        }
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        return null;
    }
}
