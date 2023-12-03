package MidEnd;

import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.HashMap;
import java.util.HashSet;

public class ActAnalysis {

    private final Module module;

    public ActAnalysis(Module module) {
        this.module = module;
    }

    public void run() {
        buildDefUseSet();
        for (Function function : module.getFunctionList()) {
            buildInOutSet(function);
        }
    }

    private void buildDefUseSet() {
        for (Function function : module.getFunctionList()) {
            for (BasicBlock block : function.getBlockArrayList()) {
                block.BuildDefUse();
            }
        }
    }

    private void buildInOutSet(Function function) {
        for (BasicBlock block : function.getBlockArrayList()) {
            block.setInSet(new HashSet<>());
            block.setOutSet(new HashSet<>());
        }
        boolean change = true;
        while (change) {
            HashMap<BasicBlock, HashSet<Value>> map = new HashMap<>();
            for (BasicBlock block : function.getBlockArrayList()) {
                map.put(block, new HashSet<>(block.getInSet()));
            }
            for (BasicBlock block : function.getBlockArrayList()) {
                HashSet<Value> s = new HashSet<>();
                for (BasicBlock block1 : block.getPosBlocks()) {
                    s.addAll(block1.getInSet());
                }
                block.setOutSet(s);
                HashSet<Value> use_b = new HashSet<>(block.getUseSet());
                HashSet<Value> Out = new HashSet<>(block.getOutSet());
                Out.removeAll(block.getDefSet());
                use_b.addAll(Out);
                block.setInSet(use_b);
            }
            change = false;
            for (BasicBlock block : function.getBlockArrayList()) {
                if (!map.get(block).equals(block.getInSet())) {
                    change = true;
                }
                map.put(block, block.getInSet());
            }
        }
    }
}
