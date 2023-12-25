package MyPackage.Parse;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.DeclLlvm;
import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.AddLlvm;
import MyPackage.IR.Instruction.AllocaLlvm;
import MyPackage.IR.Instruction.GetelementptrLlvm;
import MyPackage.IR.Instruction.LoadLlvm;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Value;
import MyPackage.Parser;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;

import java.util.ArrayList;

public class LVal {
    private String ident;
    private ArrayList<Exp> exps;

    public LVal(String ident, ArrayList<Exp> exps) {
        this.ident = ident;
        this.exps = exps;
    }

    public String getIdent() {
        return ident;
    }

    public int getLevel() {
        Symbol symbol = Parser.getCurSymbolTable().search(ident);
        if (symbol instanceof MyValSymbol) {
            return ((MyValSymbol)symbol).getLevel() - exps.size();
        }
        return exps.size();
    }

    public Value generateLlvm(MyValSymbol symbol) {
        if (symbol.getLevel() == 0) {
            return null;
        }
        Type type;
        int level = symbol.getLevel() - exps.size();
        Value value = symbol.getReg();
        boolean op = true;
        if (value.getType().equals(Type.Pointer) && !symbol.isGlobal()) {
            op = false;
            if (((AllocaLlvm)value).getArrayLlvm() != null) {
                int id = IRModule.getRegID();
                ArrayLlvm arrayLlvm = new ArrayLlvm(Type.Reg, id);
                LoadLlvm loadLlvm = new LoadLlvm(Type.Array, id, symbol);
                arrayLlvm.setDim(((AllocaLlvm)value).getArrayLlvm().getDim());
                loadLlvm.setArrayLlvm(arrayLlvm);
                loadLlvm.addOperand(value);
                loadLlvm.addOperand(value);
                value = loadLlvm;
                IRModule.curFunction.getCurrentBlock().addInstruction(loadLlvm);
            }
            else {
                Value value1 = new Value(Type.Pointer, IRModule.getRegID());
                LoadLlvm loadLlvm = new LoadLlvm(Type.Reg, value1.getValue(), symbol);
                loadLlvm.addOperand(value1);
                loadLlvm.addOperand(value);
                value = loadLlvm;
                IRModule.curFunction.getCurrentBlock().addInstruction(loadLlvm);
            }
        }
        if (level == 0) {
            type = Type.Reg;
        }
        else if (level == 1){
            type = Type.Pointer;
        }
        else {
            type = Type.Array;
        }
        ArrayList<Value> values = new ArrayList<>();
        for (int i = 0; i < exps.size(); i++) {
            values.add(exps.get(i).generateLlvm());
        }
        int regId = IRModule.getRegID();
        GetelementptrLlvm getelementptrLlvm = new GetelementptrLlvm(type, regId, symbol);
        getelementptrLlvm.addOperand(value);
        if (op) {
            getelementptrLlvm.addOperand(new Value(Type.MyConst, 0));
        }
        for (int i = 0; i < exps.size(); i++) {
            getelementptrLlvm.addOperand(values.get(i));
        }
        if (level != 0) {
            getelementptrLlvm.addOperand(new Value(Type.MyConst, 0));
        }
        IRModule.curFunction.getCurrentBlock().addInstruction(getelementptrLlvm);
        return getelementptrLlvm;
    }

    public int getIndex(DeclLlvm declLlvm) {
        if (exps.isEmpty()) {
            return 0;
        }
        else {
            int index = 0;
            int len = 1;
            for (int i = exps.size() - 1; i >= 0; i--) {
                index += exps.get(i).getValue() * len;
                len *= declLlvm.getDim().get(i);
            }
            return index;
        }
    }


}
