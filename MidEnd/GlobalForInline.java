package MidEnd;

import Ir_LLVM.LLVM_Module;
import Ir_LLVM.LLVM_Values.GlobalVar;

import java.util.ArrayList;

public class GlobalForInline {
    public static ArrayList<GlobalVar> globalVars;

    private LLVM_Module LLVMModule;

    public void setModule(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
        globalVars = new ArrayList<>(LLVMModule.getGlobalVarList());
    }

    public ArrayList<GlobalVar> getGlobalVars() {
        return globalVars;
    }
}
