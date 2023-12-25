import MyPackage.Laxer;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;
import MyPackage.Parse.CompUnit;
import MyPackage.Parser;
import MyPackage.Symbol.SymbolTable;
import MyPackage.IR.IRModule;

import java.io.File;

public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt");
        Laxer laxer = new Laxer(file);
        SymbolTable root = new SymbolTable();
        Parser parser = new Parser(laxer, root, true);
        CompUnit compUnit = parser.parseCompUnit();
        IRModule.setTool(root);
        if (!IRModule.isError) {
            compUnit.generateLlvm();
            IRModule.generateMips();
        }
        IRModule.print();
        MipsModule.print();
        OutPut.close();
    }
}