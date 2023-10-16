package FrontEnd.Symbol;

import Enums.SymbolType;

public class VarSymbol extends Symbol {

    private boolean isConst;

    private int initValue;

    private int value;

    private int dim;

    public VarSymbol(String symbolName, SymbolType symbolType, int dim, boolean isConst) {
        super(symbolName, symbolType);
        this.isConst = isConst;
        this.dim = dim;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setInitValue() {
        //TODO
    }

    public int getDim() {
        return dim;
    }

}
