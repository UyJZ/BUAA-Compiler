package FrontEnd.Symbol;

import FrontEnd.ErrorManager.RenameException;

import java.util.HashMap;
import java.util.HashSet;

public class SymbolTable {

    private final HashSet<Symbol> symbols;

    private final HashMap<String, Symbol> symbolMap;

    public SymbolTable() {
        symbols = new HashSet<>();
        symbolMap = new HashMap<>();
    }

    public void addSymbol(Symbol symbol) throws RenameException {
        if (symbolMap.containsKey(symbol.getSymbolName()))
            throw new RenameException("Rename Exception");
        symbols.add(symbol);
        symbolMap.put(symbol.getSymbolName(), symbol);
    }

    public boolean containsName(String name) {
        return symbolMap.containsKey(name) && symbolMap.get(name) instanceof VarSymbol;
    }

    public Symbol getSymbol(String name) {
        return symbolMap.get(name);
    }
}
