package llvm_ir;

import BackEnd.MIPS.Assembly.JAsm;
import BackEnd.MIPS.Assembly.JalAsm;
import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsController;
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
            f.genConStr();
        }
        LabelAsm entry = new LabelAsm("entry");
        LabelAsm end = new LabelAsm("end");
        JalAsm jal = new JalAsm(entry);
        MipsController.getInstance().addAsm(jal);
        JAsm j = new JAsm(end);
        MipsController.getInstance().addAsm(j);
        for (Function f : functionList) {
            if (f.isMainFunc()) MipsController.getInstance().addEntry(entry);
            f.genMIPS();
        }
        MipsController.getInstance().addAsm(end);
    }

    @Override
    public void genConStr() {
        for (Function function : functionList) {
            function.genConStr();
        }
    }

    public ArrayList<Function> getFunctionList() {
        return functionList;
    }
}
