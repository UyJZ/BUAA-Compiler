package FrontEnd.Symbol;

import java.util.Stack;

public class SymbolManager {

    private static final SymbolManager instance = new SymbolManager(); // 采取单例模式

    private final Stack<SymbolTable> symbolTables;

    private String currentFuncName;

    private boolean isGlobal;

    private int loopDepth;

    public SymbolManager() {
        symbolTables = new Stack<>();
        isGlobal = true;
        currentFuncName = null;
        loopDepth = 0;
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

    public Symbol getSymbol(String name) {
        //TODO: 从符号表中查找符号
        return null;
    }

    public int getLoopDepth() {
        return loopDepth;
    }

    public static SymbolManager getInstance() {
        return instance;
    }
}
