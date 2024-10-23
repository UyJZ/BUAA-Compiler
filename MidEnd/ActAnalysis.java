package MidEnd;

import IR_LLVM.LLVM_Module;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;

import java.util.HashMap;
import java.util.HashSet;

public class ActAnalysis {

    private final LLVM_Module LLVMModule;

    public ActAnalysis(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
    }

    public void run() {
        buildDefUseSet();
        for (Function function : LLVMModule.getFunctionList()) {
            buildInOutSet(function);
        }
    }

    private void buildDefUseSet() {
        for (Function function : LLVMModule.getFunctionList()) {
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
            HashMap<BasicBlock, HashSet<LLVM_Value>> map = new HashMap<>();
            for (BasicBlock block : function.getBlockArrayList()) {
                map.put(block, new HashSet<>(block.getInSet()));
            }
            for (BasicBlock block : function.getBlockArrayList()) {
                HashSet<LLVM_Value> s = new HashSet<>();
                for (BasicBlock block1 : block.getPosBlocks()) {
                    s.addAll(block1.getInSet());
                }
                // Out = \cap_{pos} In
                block.setOutSet(s);
                HashSet<LLVM_Value> use_b = new HashSet<>(block.getUseSet());
                HashSet<LLVM_Value> Out = new HashSet<>(block.getOutSet());
                // In = use + (Out - def)
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
        for (BasicBlock block : function.getBlockArrayList()) {
            System.out.println("\n" + block.hash + ": ");
            System.out.print("in: ");
            for (LLVM_Value LLVMValue : block.getInSet()) {
                System.out.print(LLVMValue.hash + " ");
            }
        }
    }
}
