package MyPackage.IR;
import MyPackage.MIPS.Instruction.Jal;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;
import MyPackage.Symbol.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class IRModule {
    private static SymbolTable root;
    private static SymbolTable curTable;
    private static int regID;
    private static int labelID;
    private static ArrayList<Value> decls;
    public static Function curFunction;
    private static Stack<Integer> forId;
    public static boolean isError = false;

    public static void setTool(SymbolTable root) {
        IRModule.root = root;
        regID = -1;
        labelID = 0;
        curTable = root;
        decls = new ArrayList<>();
        forId = new Stack<>();
    }

    public static void setCurTable(SymbolTable cur) {
        curTable = cur;
    }

    public static SymbolTable getRoot() {
        return root;
    }

    public static SymbolTable getCurTable() {
        return curTable;
    }

    public static int getRegID() {
        regID++;
        return regID;
    }

    public static int getLabelID() {
        labelID++;
        return labelID;
    }

    public static void resetID() {
        regID = -1;
    }

    public static ArrayList<Value> getDecls() {
        return decls;
    }

    public static void addDecls(Value value) {
        decls.add(value);
    }


    public static void print() {
        OutPut.printLlvm("declare i32 @getint()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @putstr(i8*)\n");
        for (int i = 0; i < decls.size(); i++) {
            decls.get(i).print();
        }
    }

    public static void generateMips() {
        MipsModule.addText(new Jal("main"));
        for (int i = 0; i < decls.size(); i++) {
            decls.get(i).generateMips();
        }
    }

    public static void addForId(int id) {
        forId.push(id);
    }

    public static int getForId() {
        return forId.peek();
    }

    public static void popForId() {
        forId.pop();
    }

    public static boolean isError() {
        return isError;
    }

    public static void setError(boolean error) {
        isError = error;
    }

}
