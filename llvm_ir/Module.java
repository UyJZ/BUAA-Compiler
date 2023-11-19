package llvm_ir;

import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.llvmType.ModelType;

import java.util.ArrayList;

public class Module extends Value {
    private ArrayList<GlobalVar> globalVarList;
    private ArrayList<Function> functionList;

    private static String InitialFunc = "declare i32 @getint()\n" +
            "declare void @putint(i32)\n" +
            "declare void @putch(i32)\n" +
            "declare void @putstr(i8*)";

    public Module() {
        super(new ModelType(), "CompUnit");
        this.globalVarList = new ArrayList<>();
        this.functionList = new ArrayList<>();
    }

    public void addGlobalVar(GlobalVar globalVar) {
        globalVarList.add(globalVar);
    }

    public void addFunction(Function function) {
        functionList.add(function);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(InitialFunc).append("\n");
        for (GlobalVar g : globalVarList) {
            sb.append(g).append("\n");
        }
        for (Function f : functionList) {
            sb.append(f).append("\n");
        }
        return sb.toString();
    }

    public void setName() {
        for (Function f : functionList) {
            f.setName();
        }
    }

    @Override
    public void genMIPS() {
        for (GlobalVar var : globalVarList) {
            var.genMIPS();
        }
        for (Function f : functionList) {
            f.genMIPS();
        }
    }
}
