package MyPackage.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> map;
    private SymbolTable pre;
    private ArrayList<SymbolTable> nextTable;
    private int index;

    public SymbolTable() {
        map = new HashMap<>();
        nextTable = new ArrayList<>();
        pre = null;
        index = 0;
    }

    public SymbolTable(SymbolTable pre) {
        map = new HashMap<>();
        nextTable = new ArrayList<>();
        this.pre = pre;
        index = 0;
    }

    public void addTable(SymbolTable symbolTable) {
        nextTable.add(symbolTable);
    }

    public void addSymbol(String name, Symbol symbol) {
        map.put(name, symbol);
    }

    public boolean contain(String name) {
        return map.containsKey(name);
    }

    public Symbol search(String name) {
        SymbolTable symbolTable = this;
        while (true) {
            if (symbolTable == null) {
                return null;
            }
            if (symbolTable.getMap().containsKey(name)) {
                return symbolTable.getMap().get(name);
            }
            else {
                symbolTable = symbolTable.getPre();
            }
        }
    }

    public SymbolTable getPre() {
        return pre;
    }
    public HashMap<String, Symbol> getMap() {
        return map;
    }

    public SymbolTable getNextTable() {
        index++;
        return nextTable.get(index - 1);
    }
}
