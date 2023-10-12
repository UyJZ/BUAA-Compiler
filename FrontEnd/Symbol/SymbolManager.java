package FrontEnd.Symbol;

import java.util.Stack;

public class SymbolManager {

    private Stack<SymbolTable> symbolTables;

    public SymbolManager() {
        symbolTables = new Stack<>();
    }

    public void addSymbol(Symbol symbol) {
        symbolTables.peek().addSymbol(symbol);
    }

    public void popSymbolTable() {
        symbolTables.pop();
    }

    public void pushSymbolTable() {
        symbolTables.push(new SymbolTable());
    }

}
