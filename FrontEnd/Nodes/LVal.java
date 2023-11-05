package FrontEnd.Nodes;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.LoadInstr;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;
import java.util.List;

public class LVal extends Node {

    private final String name;

    public LVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
    }

    public int identLine() {
        return children.get(0).getEndLine();
    }

    public String getName() {
        return name;
    }

    @Override
    public int getDim() {
        int dim = SymbolManager.getInstance().getDimByName(name);
        for (Node child : children) if (child instanceof Exp) dim--;
        return dim;
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getTokenType() == tokenType.IDENFR)
            if (!SymbolManager.getInstance().isVarDefined(((TokenNode) children.get(0)).getIdentName()))
                ErrorChecker.AddError(new Error(identLine(), ErrorType.c));
        super.checkError();
    }

    public int calc() {
        String name = ((TokenNode) children.get(0)).getIdentName();
        Symbol symbol = SymbolManager.getInstance().getSymbolByName(name);
        if (symbol == null) return -1;
        else {
            VarSymbol varSymbol = (VarSymbol) symbol;
            List<Integer> pos = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) pos.add(((Exp) n).calc());
            }
            return varSymbol.getValue(pos);
        }
    }

    @Override
    public Value genLLVMir() {
        VarSymbol symbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            if (symbol.isGlobal() && !SymbolManager.getInstance().isGlobal()) {
                LoadInstr instr = new LoadInstr(new Integer32Type(), symbol.getSymbolName());
                IRController.getInstance().addInstr(instr);
                return instr;
            } else {
                LoadInstr instr = new LoadInstr(new Integer32Type(), symbol.getLLVMirValue().getName());
                IRController.getInstance().addInstr(instr);
                return instr;
            }
        } else {
            //getelementptr
        }
        return null;
    }
}
