package FrontEnd.SymbolTable.Symbols;

import FrontEnd.SymbolTable.SymbolType;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.InitializedValue;
import IR_LLVM.LLVM_Types.ArrayType;
import IR_LLVM.LLVM_Types.Integer32Type;
import IR_LLVM.LLVM_Types.LLVMType;
import IR_LLVM.LLVM_Types.PointerType;

import java.util.ArrayList;
import java.util.List;

public class VarSymbol extends Symbol {

    private boolean isConst;

    private boolean isGlobal;

    private boolean isCalcAble;

    private InitializedValue initializedValue = null;

    private int value;

    private LLVMType type;

    private int dim;

    private boolean isParam = false;

    private Integer val = null;

    private ArrayList<Integer> lens = new ArrayList<>();

    public VarSymbol(String symbolName, SymbolType symbolType, int dim, boolean isConst) {
        super(symbolName, symbolType);
        this.isConst = isConst;
        this.dim = dim;
        this.isGlobal = SymbolTableBuilder.getInstance().isGlobal();
        this.type = symbolType == SymbolType.SYMBOL_VAR ? new IR_LLVM.LLVM_Types.Integer32Type() : new IR_LLVM.LLVM_Types.VoidType();
        isCalcAble = false;
    }

    public VarSymbol(String symbolName, SymbolType symbolType, int dim, boolean isConst, ArrayList<Integer> lens) {
        super(symbolName, symbolType);
        this.isConst = isConst;
        this.dim = dim;
        this.lens = lens;
        this.isGlobal = SymbolTableBuilder.getInstance().isGlobal();
        if (dim == 0) this.type = new Integer32Type();
        else this.type = new ArrayType(lens, new Integer32Type());
        isCalcAble = false;
    }

    public VarSymbol(String symbolName, SymbolType symbolType, int dim, boolean isConst, int width) { // for funcfparam
        super(symbolName, symbolType);
        this.isConst = isConst;
        this.dim = dim;
        this.isGlobal = SymbolTableBuilder.getInstance().isGlobal();
        if (dim == 0) type = new Integer32Type();
        else if (dim == 1) type = new PointerType(new Integer32Type());
        else if (dim == 2) {
            ArrayList<Integer> l = new ArrayList<>();
            l.add(width);
            type = new PointerType(new ArrayType(l, new Integer32Type()));
        }
        isCalcAble = false;
    }

    public void setAsParam() {
        isParam = true;
    }

    public boolean isParam() {
        return isParam;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setInitValue(InitializedValue initializedValue) {
        this.initializedValue = initializedValue;
        if (dim == 0) {
            this.val = initializedValue.getVal(new ArrayList<>());
            isCalcAble = false;
        }
    }

    public ArrayList<Integer> getLens() {
        return lens;
    }

    public int getDim() {
        return dim;
    }

    public InitializedValue getInitial() {
        return initializedValue;
    }

    public int getValue(List<Integer> pos) {
        return initializedValue.getVal(pos);
    }


    public LLVMType getType() {
        return type;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public boolean isCalcAble() {
        return isCalcAble;
    }

    public void setChanged() {
        isCalcAble = false;
    }

    public void setValFor0Dim(int val) {
        this.val = val;
    }

    public Integer getValueFor0Dim() {
        return val;
    }
}
