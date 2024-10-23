package IR_LLVM;

import BackEnd.MIPS.Assembly.JAsm;
import BackEnd.MIPS.Assembly.JalAsm;
import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsBuilder;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.GlobalVar;
import IR_LLVM.LLVM_Types.ModelType;

import java.util.ArrayList;

public class LLVM_Module extends LLVM_Value {
    private final ArrayList<GlobalVar> globalVarList;
    private final ArrayList<Function> functionList;

    private static final String InitialFunc = "declare i32 @getint()\n" +
            "declare void @putint(i32)\n" +
            "declare void @putch(i32)\n" +
            "declare void @putstr(i8*)";

    public LLVM_Module() {
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

    public void clearName() {
        for (Function f : functionList) {
            f.clearName();
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
        MipsBuilder.getInstance().addAsm(jal);
        JAsm j = new JAsm(end);
        MipsBuilder.getInstance().addAsm(j);
        for (Function f : functionList) {
            if (f.isMainFunc()) MipsBuilder.getInstance().addEntry(entry);
            f.genMIPS();
        }
        MipsBuilder.getInstance().addAsm(end);
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

    public ArrayList<GlobalVar> getGlobalVarList() {
        return globalVarList;
    }
}
