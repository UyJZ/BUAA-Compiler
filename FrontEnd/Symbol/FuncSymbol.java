package FrontEnd.Symbol;

import Enums.SymbolType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol{

    private String funcName;

    private ArrayList<VarSymbol> params;

    public FuncSymbol(String symbolName, SymbolType symbolType) {
        super(symbolName, symbolType);
    }

}
