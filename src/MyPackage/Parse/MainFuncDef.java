package MyPackage.Parse;

import MyPackage.IR.Function;
import MyPackage.IR.Type;
import MyPackage.IR.IRModule;

public class MainFuncDef implements Decl{
    private Block block;

    public MainFuncDef(Block block) {
        this.block = block;
    }

    public void generateLlvm(boolean isGlobal) {
        Function function = new Function(Type.Int, 0, "main");
        IRModule.curFunction = function;
        IRModule.curFunction.newBlock();
        IRModule.resetID();
        IRModule.getRegID();
        block.generateLlvm(true);
        IRModule.addDecls(function);
    }


}
