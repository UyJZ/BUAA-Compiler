package FrontEnd.Symbol;

import java.util.HashSet;

public class SymbolTable {

    private HashSet<Symbol> symbols;

    public SymbolTable() {
        symbols = new HashSet<>();
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }
}
