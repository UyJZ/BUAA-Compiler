package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.Function;

public class MergeBlock {
    private final Module module;

    public MergeBlock(Module module) {
        this.module = module;
    }

    public void run() {
        Merge();
    }

    private void Merge() {
        for (Function function : module.getFunctionList()) {
            for (int i = 0; i < function.getBlockArrayList().size(); i++) {
                for (int j = i + 1; j < function.getBlockArrayList().size(); j++) {
                    if (function.getBlockArrayList().get(i).canMerged(function.getBlockArrayList().get(j))) {
                        function.getBlockArrayList().get(i).merge(function.getBlockArrayList().get(j));
                        function.getBlockArrayList().remove(j);
                        j--;
                    }
                }
            }
        }
    }
}
