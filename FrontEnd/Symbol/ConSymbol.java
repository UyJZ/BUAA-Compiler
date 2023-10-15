package FrontEnd.Symbol;

import Enums.SymbolType;

import java.util.ArrayList;

public class ConSymbol extends Symbol {

    private int dim;

    private ArrayList<Integer> initValue;

    private ArrayList<Integer> lengthList;

    public ConSymbol(String symbolName, SymbolType symbolType, ArrayList<Integer> initValue, ArrayList<Integer> lengthList) {
        super(symbolName, symbolType);
        this.initValue = initValue;
        this.lengthList = lengthList;
        this.dim = lengthList.size();
    }

    @Override
    public int getDim() {
        return dim;
    }
}
