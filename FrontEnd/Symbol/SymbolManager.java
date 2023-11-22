package FrontEnd.Symbol;

import FrontEnd.ErrorManager.RenameException;

import java.util.*;

public class SymbolManager {

    private static final SymbolManager instance = new SymbolManager(); // 采取单例模式
    private final Stack<SymbolTable> symbolTableStack;

    private final HashMap<String, SymbolTable> funcMap;

    private final HashMap<String, FuncSymbol> funcSymbolHashMap;

    private String currentFuncName;

    private boolean isGlobal;

    private int loopDepth;

    public SymbolManager() {
        symbolTableStack = new Stack<>();
        funcMap = new HashMap<>();
        isGlobal = true;
        currentFuncName = null;
        loopDepth = 0;
        funcSymbolHashMap = new HashMap<>();
    }

    public int getLoopDepth() {
        return loopDepth;
    }

    public void enterFuncBlock(FuncSymbol funcSymbol) throws RenameException {
        if (getInstance().getSymbolByName(funcSymbol.getSymbolName()) != null || getInstance().getFuncSymbolByFuncName(funcSymbol.getSymbolName()) != null)
            throw new RenameException("Rename Exception");
        SymbolTable symbolTable = new SymbolTable(funcSymbol);
        symbolTableStack.push(symbolTable);
        funcMap.put(funcSymbol.getSymbolName(), symbolTable);
        currentFuncName = funcSymbol.getSymbolName();
        //symbolTableStack.peek().addSymbol(funcSymbol);
    }

    public void enterBlock() {
        symbolTableStack.push(new SymbolTable());
    }

    public void enterLoopBlock() {
        loopDepth++;
    }

    public void leaveBlock() {
        symbolTableStack.pop();
    }

    public void leaveLoopBlock() {
        loopDepth--;
    }

    public void addSymbol(Symbol symbol) throws RenameException {
        SymbolTable topTable = symbolTableStack.peek();
        topTable.addSymbol(symbol);
        if (symbol instanceof FuncSymbol funcSymbol) funcSymbolHashMap.put(funcSymbol.getSymbolName(), funcSymbol);
    }

    public void addSysSymbol(FuncSymbol symbol) throws RenameException {
        funcSymbolHashMap.put(symbol.getSymbolName(), symbol);
    }

    public boolean isVarDefined(String name) {
        for (SymbolTable s : symbolTableStack) {
            if (s.containsName(name)) return true;
        }
        return false;
    }

    public boolean isVarFuncDefined(String name) {
        for (SymbolTable s : symbolTableStack) {
            if (s.containsVarFuncName(name)) return true;
        }
        return funcMap.containsKey(name);
    }

    public int getDimByName(String name) {
        for (SymbolTable s : symbolTableStack) {
            if (s.containsName(name)) return s.getSymbol(name).getDim();
        }
        if (funcMap.get(name) != null) return funcMap.get(name).getFuncSymbol(name).getDim();
        return -1;
    }

    public Symbol getSymbolByName(String name) {
        List<SymbolTable> list = new ArrayList<>(symbolTableStack);
        Collections.reverse(list);
        for (SymbolTable s : list) {
            if (s.containsName(name)) return s.getSymbol(name);
        }
        return null;
    }

    public FuncSymbol getFuncSymbolByFuncName(String name) {
        if (funcMap.containsKey(name)) {
            return (FuncSymbol) funcMap.get(name).getFuncSymbol(name);
        } else if (funcSymbolHashMap.containsKey(name)) return funcSymbolHashMap.get(name);
        return null;
    }

    public String getCurrentFuncName() {
        return currentFuncName;
    }

    public static SymbolManager getInstance() {
        return instance;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public void flush() {
        symbolTableStack.clear();
        funcMap.clear();
        isGlobal = true;
        currentFuncName = null;
        loopDepth = 0;
    }
}
