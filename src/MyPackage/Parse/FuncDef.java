package MyPackage.Parse;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.Function;
import MyPackage.IR.IRModule;
import MyPackage.IR.Type;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class FuncDef implements Decl{
    private String type;
    private String ident;
    private FuncFParams funcFParams;
    private Block block;

    public FuncDef(String type, String ident, FuncFParams funcFParams, Block block) {
        this.type = type;
        this.funcFParams = funcFParams;
        this.block = block;
        this.ident = ident;
    }

    public void generateLlvm(boolean isGlobal) {
        Function function;
        if (type.equals("void")) {
            function =  new Function(Type.Void, 0, ident);
        }
        else {
            function =  new Function(Type.Int, 0, ident);
        }
        IRModule.curFunction = function;
        IRModule.curFunction.newBlock();
        IRModule.resetID();
        if (funcFParams != null) {
            funcFParams.generateLlvm();
        }
        IRModule.getRegID();
        block.generateLlvm(true);
        IRModule.addDecls(function);
    }
}
