package FrontEnd.Symbol;

import Enums.SymbolType;

public class ConSymbol extends Symbol{

    int value;
    public ConSymbol(String symbolName, SymbolType symbolType, int value) {
        super(symbolName, symbolType);
        this.value = value;
    }
}
