package FrontEnd.Symbol;

import FrontEnd.ErrorManager.RenameException;

import java.util.HashMap;
import java.util.HashSet;

public class SymbolTable {

    private final HashSet<Symbol> symbols;

    private final FuncSymbol funcSymbol;

    private final HashMap<String, Symbol> symbolMap;

    public SymbolTable() {
        symbols = new HashSet<>();
        symbolMap = new HashMap<>();
        funcSymbol = null;
    }

    public SymbolTable(FuncSymbol funcSymbol) {
        symbols = new HashSet<>();
        symbolMap = new HashMap<>();
        this.funcSymbol = funcSymbol;
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

    public boolean containsVarFuncName(String name) {
        return symbolMap.containsKey(name);
    }

    public Symbol getSymbol(String name) {
        return symbolMap.get(name);
    }

    public FuncSymbol getFuncSymbol(String name) {
        if (funcSymbol == null || !funcSymbol.getSymbolName().equals(name)) return null;
        else return funcSymbol;
    }
}
