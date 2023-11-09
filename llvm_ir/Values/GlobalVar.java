package llvm_ir.Values;

import FrontEnd.Symbol.Initial;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.Value;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.Integer32Type;

import java.util.ArrayList;
import java.util.List;

public class GlobalVar extends Value {

    private final ArrayList<Integer> lens;

    private final boolean isConst;

    private final Initial initial;

    public GlobalVar(VarSymbol symbol) {
        super(symbol.getDim() == 0 ? new Integer32Type() : new ArrayType(symbol.getLens(), new Integer32Type()),
                "@" + symbol.getSymbolName());
        this.lens = (ArrayList<Integer>) symbol.getLens();
        this.isConst = symbol.isConst();
        initial = symbol.getInitial();
        symbol.setLlvmValue(this);
    }

    public int getDim() {
        return lens.size();
    }

    @Override
    public String toString() {
        String isGlobal = isConst ? "constant " : "global ";
        if (getDim() == 0) {
            if (initial == null) return name + " = " + "dso_local " + isGlobal + "i32 0";
            else return name + " = "  + "dso_local " +  isGlobal + "i32 " + initial;
        } else if (getDim() == 1) {
            if(initial == null) {
                return name + " = " + "dso_local " + isGlobal + "[" + lens.get(0) + " x i32] zeroinitializer";
            } else {
                return name + " = " + "dso_local " + isGlobal + initial.GlobalVarLLVMir(lens, new Integer32Type());
            }
        } else {
            if (initial == null) {
                return name + " = " + "dso_local " + isGlobal + "[" + lens.get(0) + " x " + "[" + lens.get(1) + " x i32]] zeroinitializer";
            } else {
                return name + " = " + "dso_local " + isGlobal + initial.GlobalVarLLVMir(lens, new Integer32Type());
            }
        }
    }
}
