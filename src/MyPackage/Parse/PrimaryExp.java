package MyPackage.Parse;


import MyPackage.IR.DeclLlvm;
import MyPackage.IR.Instruction.LoadLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.IR.IRModule;
import MyPackage.Symbol.Symbol;
import MyPackage.Symbol.SymbolTable;

import java.util.ArrayList;

public class PrimaryExp {
    private int type;
    private Exp exp;
    private LVal lVal;
    private int number;

    public PrimaryExp(Exp exp) {
        type = 0;
        this.exp = exp;
    }

    public PrimaryExp(LVal lVal) {
        type = 1;
        this.lVal = lVal;
    }

    public PrimaryExp(int number) {
        type = 2;
        this.number = number;
    }

    public int getLevel() {
        if (type != 1) {
            return 0;
        }
        return lVal.getLevel();
    }

    public Value generateLlvm() {
        if (type == 0) {
            return exp.generateLlvm();
        }
        else if (type == 1) {
            Symbol symbol = IRModule.getCurTable().search(lVal.getIdent());
            SymbolTable symbolTable = IRModule.getCurTable();
            while (!(symbol instanceof MyValSymbol) || ((MyValSymbol)symbol).getReg() == null) {
                symbolTable = symbolTable.getPre();
                symbol = symbolTable.search(lVal.getIdent());
            }
            Value value = lVal.generateLlvm((MyValSymbol) symbol);
            if (value == null) {
                int regId = IRModule.getRegID();
                LoadLlvm loadLlvm = new LoadLlvm(Type.Reg, regId, symbol);;
                loadLlvm.addOperand(loadLlvm);
                loadLlvm.addOperand(((MyValSymbol) symbol).getReg());
                ((MyValSymbol)symbol).getReg().addUse(new Use( ((MyValSymbol)symbol).getReg(), loadLlvm, loadLlvm.getPos()));
                IRModule.curFunction.getCurrentBlock().addInstruction(loadLlvm);
                return loadLlvm;
            }
            else {
                if (value.getType().equals(Type.Reg)) {
                    int regId = IRModule.getRegID();
                    LoadLlvm loadLlvm = new LoadLlvm(Type.Reg, regId, symbol);;
                    loadLlvm.addOperand(loadLlvm);
                    loadLlvm.addOperand(value);
                    value.addUse(new Use(value, loadLlvm, 0));
                    IRModule.curFunction.getCurrentBlock().addInstruction(loadLlvm);
                    return loadLlvm;
                }
                else {
                    return value;
                }
            }
        }
        else {
            return new Value(Type.MyConst, number);
        }
    }

    public int getValue() {
        if (type == 0) {
            return exp.getValue();
        }
        else if (type == 1) {
            MyValSymbol symbol = (MyValSymbol) IRModule.getCurTable().search(lVal.getIdent());

            return ((DeclLlvm)(symbol.getReg())).getVal().get(lVal.getIndex((DeclLlvm)symbol.getReg()));
        }
        else {
            return number;
        }
    }
}
