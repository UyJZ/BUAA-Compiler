package FrontEnd.Symbol;

import Enums.FunctionType;
import Enums.SymbolType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {

    private String funcName;

    private FunctionType functionType;

    private ArrayList<Integer> dimList = new ArrayList<>();

    private int paramNum;

    public FuncSymbol(String symbolName, SymbolType symbolType, FunctionType functionType, ArrayList<Integer> dimList) {
        super(symbolName, symbolType);
        this.functionType = functionType;
        this.dimList = dimList;
        this.paramNum = dimList.size();
    }

    @Override
    public int getDim() {
        return (functionType == FunctionType.FUNC_INT) ? 0 : -1;
    }

    public ArrayList<Integer> getDimList() {
        return dimList;
    }

}
