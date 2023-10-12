package FrontEnd.Symbol;

import Enums.SymbolType;

public class Symbol {

    private final String symbolName;

    private final SymbolType symbolType;

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
}
