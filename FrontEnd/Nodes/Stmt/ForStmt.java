package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.LVal;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;

public class ForStmt extends Node {

    private LVal lVal;

    private Exp expr;

    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value genLLVMir() {
        Value operand = children.get(2).genLLVMir();
        for (Node node : children) {
            if (node instanceof LVal) lVal = (LVal) node;
            else if (node instanceof Exp) expr = (Exp) node;
        }
        VarSymbol symbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(lVal.getName());
        if (symbol.isGlobal()) {
            StoreInstr instr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), operand.getName(), "@" + lVal.getName());
            IRController.getInstance().addInstr(instr);
            return instr;
        } else {
            StoreInstr instr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), operand.getName(), symbol.getLLVMirValue().getName());
            IRController.getInstance().addInstr(instr);
            return instr;
        }
    }
}
