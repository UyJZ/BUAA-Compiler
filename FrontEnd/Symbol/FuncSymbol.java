package FrontEnd.Symbol;

import Enums.FuncType;
import Enums.SymbolType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {

    private String funcName;

    private FuncType funcType;

    private ArrayList<Integer> dimList = new ArrayList<>();

    private int paramNum;

    public FuncSymbol(String symbolName, SymbolType symbolType, FuncType funcType, ArrayList<Integer> dimList) {
        super(symbolName, symbolType);
        this.funcType = funcType;
        this.dimList = dimList;
        this.paramNum = dimList.size();
    }

    @Override
    public int getDim() {
        return (funcType == FuncType.FUNC_INT) ? 0 : -1;
    }

    public ArrayList<Integer> getDimList() {
        return dimList;
    }

}
