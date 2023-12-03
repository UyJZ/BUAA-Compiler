package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
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
                        BasicBlock block1 = function.getBlockArrayList().get(i);
                        BasicBlock block2 = function.getBlockArrayList().get(j);
                        if (block2.getPreBlocks().size() == 1 && block2.getPreBlocks().get(0) == block1 && block1.getPosBlocks().size() == 1 && block1.getPosBlocks().get(0) == block2) {
                            block1.removeInstr(block1.lastInstr());
                            for (int k = 0; k < block2.getInstrs().size(); k++) {
                                block1.addInstr(block2.getInstrs().get(k));
                            }
                            block2.replacedBy(block1);
                            function.getBlockArrayList().remove(j);
                            j--;
                        }
                        /*
                        else {
                            block1.removeInstr(block1.lastInstr());
                            block1.replacedBy(block2);
                            function.getBlockArrayList().remove(i);
                            i--;
                            break;
                        }
                         */
                    }
                }
            }
        }
    }
}
