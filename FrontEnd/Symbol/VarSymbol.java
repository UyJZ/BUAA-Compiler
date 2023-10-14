package FrontEnd.Symbol;

import Enums.SymbolType;

public class VarSymbol extends Symbol {

    private boolean isGlobal;

    private int initValue;

    private int value;

    private int dim;

    public VarSymbol(String symbolName, SymbolType symbolType) {
        super(symbolName, symbolType);
    }

}
