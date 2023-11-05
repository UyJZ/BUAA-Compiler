package FrontEnd.Symbol;

import Enums.FunctionType;
import Enums.SymbolType;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {

    private String funcName;

    private FunctionType functionType;

    private ArrayList<Integer> dimList = new ArrayList<>();

    private ArrayList<LLVMType> types = new ArrayList<>();

    private LLVMType type;

    private int paramNum;

    public FuncSymbol(String symbolName, SymbolType symbolType, FunctionType functionType, ArrayList<Integer> dimList) {
        super(symbolName, symbolType);
        this.functionType = functionType;
        this.dimList = dimList;
        this.paramNum = dimList.size();
        this.type = (functionType == FunctionType.FUNC_INT) ? new llvm_ir.llvmType.Integer32Type() : new llvm_ir.llvmType.VoidType();
    }

    public FuncSymbol(String symbolname, SymbolType symbolType, FunctionType functionType, ArrayList<Integer> dimList, ArrayList<LLVMType> types) {
        super(symbolname, symbolType);
        this.functionType = functionType;
        this.dimList = dimList;
        this.types = types;
        this.paramNum = dimList.size();
        this.type = (functionType == FunctionType.FUNC_INT) ? new llvm_ir.llvmType.Integer32Type() : new llvm_ir.llvmType.VoidType();
    }

    @Override
    public int getDim() {
        return (functionType == FunctionType.FUNC_INT) ? 0 : -1;
    }

    public ArrayList<Integer> getDimList() {
        return dimList;
    }

    public LLVMType getLLVMType() {
        return type;
    }

}
