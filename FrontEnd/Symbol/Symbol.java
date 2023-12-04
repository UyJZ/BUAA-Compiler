package FrontEnd.Symbol;

import Enums.SymbolType;
import llvm_ir.Value;

public class Symbol {

    private final String symbolName;

    private final SymbolType symbolType;

    protected Value llvmValue;// 在llvm中，一切皆为值，这个是记录赋值的指令

    public Symbol(String symbolName, SymbolType symbolType) {
        this.symbolName = symbolName;
        this.symbolType = symbolType;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setLlvmValue(Value llvmValue) {
        this.llvmValue = llvmValue;
    }

    public Value getLLVMirValue() {
        return llvmValue;
    }


    public int getDim() {
        return -1;
    }
}
