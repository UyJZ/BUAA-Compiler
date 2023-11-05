package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.LVal;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;
import java.util.Calendar;

public class GetIntStmt extends Stmt {

    private LVal lVal;

    public GetIntStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node node : children) if (node instanceof LVal) lVal = (LVal) node;
    }

    @Override
    public void checkError() {
        VarSymbol varSymbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(lVal.getName());
        if (varSymbol.isConst()) ErrorChecker.AddError(new Error(lVal.identLine(), ErrorType.h));
        super.checkError();
    }

    @Override
    public Value genLLVMir() {
        Symbol symbol = SymbolManager.getInstance().getSymbolByName(lVal.getName());
        CallInstr callInstr = new CallInstr(new Integer32Type(), "@getint", new ArrayList<>(), IRController.getInstance().genVirtualRegNum());
        IRController.getInstance().addInstr(callInstr);
        StoreInstr storeInstr = new StoreInstr(callInstr.getType(), new PointerType(new Integer32Type()), callInstr.getName(), ((VarSymbol) symbol).getLLVMirValue().getName());
        IRController.getInstance().addInstr(storeInstr);
        return storeInstr;
    }
}
