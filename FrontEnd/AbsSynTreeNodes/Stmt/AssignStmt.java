package FrontEnd.AbsSynTreeNodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.AbsSynTreeNodes.LVal;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.SymbolTable.*;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.StoreInstr;

import java.util.ArrayList;

public class AssignStmt extends Stmt {

    private LVal lVal;

    private Exp expr;

    public AssignStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node node : children) {
            if (node instanceof LVal) lVal = (LVal) node;
            else if (node instanceof Exp) expr = (Exp) node;
        }
    }

    @Override
    public void checkError() {
        super.checkError();
        VarSymbol varSymbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        if (varSymbol != null && varSymbol.isConst()) ErrorList.AddError(new Error(lVal.identLine(), ErrorType.h));
    }

    @Override
    public LLVM_Value genLLVMir() {
        LLVM_Value operand = children.get(2).genLLVMir();
        assert (children.get(0) instanceof LVal lVal);
        VarSymbol varSymbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(lVal.getName());
        LLVM_Value operand1 = lVal.genLLVMForAssign();
        if (operand instanceof ConstInteger constInteger) {
            if (varSymbol.getDim() == 0)
                varSymbol.setValFor0Dim(constInteger.getVal());
            StoreInstr instr = new StoreInstr(constInteger, operand1);
            LLVM_Builder.getInstance().addInstr(instr);
            return instr;
        } else {
            StoreInstr instr = new StoreInstr(operand, operand1);
            LLVM_Builder.getInstance().addInstr(instr);
            varSymbol.setChanged();
            return instr;
        }
    }
}
