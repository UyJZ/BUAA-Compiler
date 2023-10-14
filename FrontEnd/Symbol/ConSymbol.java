package FrontEnd.Symbol;

import Enums.SymbolType;

import java.util.ArrayList;

public class ConSymbol extends Symbol{

    private int dim;

    private ArrayList<Integer> initValue;

    int value;
    public ConSymbol(String symbolName, SymbolType symbolType, int value) {
        super(symbolName, symbolType);
        this.value = value;
    }
}
