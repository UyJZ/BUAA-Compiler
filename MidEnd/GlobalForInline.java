package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.GlobalVar;

import java.util.ArrayList;

public class GlobalForInline {
    public static ArrayList<GlobalVar> globalVars;

    private Module module;

    public void setModule(Module module) {
        this.module = module;
        globalVars = new ArrayList<>(module.getGlobalVarList());
    }

    public ArrayList<GlobalVar> getGlobalVars() {
        return globalVars;
    }
}
