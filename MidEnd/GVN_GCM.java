package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.HashMap;

public class GVN_GCM {

    private final Module module;

    public GVN_GCM(Module module) {
        this.module = module;
    }

    public void run() {
        GVN();
    }

    private void GVN() {
        for (Function function : module.getFunctionList()) {
            BasicBlock entry = function.getBlockArrayList().get(0);
            entry.proOrderForGVN(function.getInstrGVNMap());
        }
    }
}