package FrontEnd.SymbolTable.Symbols;

import FrontEnd.SymbolTable.SymbolType;
import IR_LLVM.LLVM_Value;

public class Symbol {

    private final String symbolName;

    private final SymbolType symbolType;

    protected LLVM_Value llvmLLVMValue;// 在llvm中，一切皆为值，这个是记录赋值的指令

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

    public void setLlvmValue(LLVM_Value llvmLLVMValue) {
        this.llvmLLVMValue = llvmLLVMValue;
    }

    public LLVM_Value getLLVMirValue() {
        return llvmLLVMValue;
    }


    public int getDim() {
        return -1;
    }
}
