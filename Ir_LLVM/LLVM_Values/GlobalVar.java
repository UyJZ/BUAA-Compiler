package Ir_LLVM.LLVM_Values;

import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsBuilder;
import Ir_LLVM.InitializedValue;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Types.ArrayType;
import Ir_LLVM.LLVM_Types.Integer32Type;
import Ir_LLVM.LLVM_Types.PointerType;

import java.util.ArrayList;

public class GlobalVar extends LLVM_Value {

    private final ArrayList<Integer> lens;

    private final LabelAsm label;

    private final boolean isConst;

    private final InitializedValue initializedValue;

    public GlobalVar(VarSymbol symbol) {
        super(new PointerType(symbol.getDim() == 0 ? new Integer32Type() : new ArrayType(symbol.getLens(), new Integer32Type())),
                "@" + symbol.getSymbolName());
        this.lens = (ArrayList<Integer>) symbol.getLens();
        this.isConst = symbol.isConst();
        initializedValue = symbol.getInitial();
        symbol.setLlvmValue(this);
        this.label = new LabelAsm("global_" + symbol.getSymbolName());
    }

    public int getDim() {
        return lens.size();
    }

    @Override
    public String toString() {
        String isGlobal = isConst ? "constant " : "global ";
        if (getDim() == 0) {
            if (initializedValue == null) return name + " = " + "dso_local " + isGlobal + "i32 0";
            else return name + " = " + "dso_local " + isGlobal + "i32 " + initializedValue;
        } else if (getDim() == 1) {
            if (initializedValue == null) {
                return name + " = " + "dso_local " + isGlobal + "[" + lens.get(0) + " x i32] zeroinitializer";
            } else {
                return name + " = " + "dso_local " + isGlobal + initializedValue.GlobalVarLLVMir(lens, new Integer32Type());
            }
        } else {
            if (initializedValue == null) {
                return name + " = " + "dso_local " + isGlobal + "[" + lens.get(0) + " x " + "[" + lens.get(1) + " x i32]] zeroinitializer";
            } else {
                return name + " = " + "dso_local " + isGlobal + initializedValue.GlobalVarLLVMir(lens, new Integer32Type());
            }
        }
    }

    @Override
    public void genMIPS() {
        Data data = new Data(lens, initializedValue, name);
        this.data = data;
        MipsBuilder.getInstance().addGlobalVar(data);
        this.isDistributed = true;
    }

    public LabelAsm getLabel() {
        return label;
    }

    @Override
    public boolean isDistributable() {
        return false;
    }
}
