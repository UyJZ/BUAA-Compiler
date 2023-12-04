package MidEnd;

import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.PhiInstr;

import java.util.*;

public class SSABuilder {
    private final Module module;

    public SSABuilder(Module module) {
        this.module = module;
    }

    public void run() {
        insertPhiInstr();
    }

    private void insertPhiInstr() {
        for (Function f : module.getFunctionList()) {
            ArrayList<AllocaInst> values = f.getValForSSA();
            HashMap<AllocaInst, LinkedHashSet<BasicBlock>> defBlocks = new HashMap<>();
            for (AllocaInst value : values) {
                defBlocks.put(value, new LinkedHashSet<>());
            }
            for (AllocaInst v : values) {
                for (BasicBlock block : f.getBlockArrayList()) {
                    if (block.isDefBlockFor(v)) {
                        defBlocks.get(v).add(block);
                    }
                }
                LinkedHashSet<BasicBlock> F = new LinkedHashSet<>();
                LinkedHashSet<BasicBlock> W = new LinkedHashSet<>(defBlocks.get(v));
                while (!W.isEmpty()) {
                    BasicBlock x = W.iterator().next();
                    W.remove(x);
                    for (BasicBlock y : x.getDominateFrontier()) {
                        if (!F.contains(y)) {
                            //TODO:add phi at the entry of y
                            PhiInstr phi = new PhiInstr(v.getElementType(), v);
                            y.insertPhi(phi);
                            f.addDef(v, phi);
                            F.add(y);
                            if (!defBlocks.get(v).contains(y)) {
                                W.add(y);
                            }
                        }
                    }
                }
                //TODO:应该马上进行重命名
                //rename
                BasicBlock entry = f.getBlockArrayList().get(0);
                //马上生成严格支配树
            }
            for (AllocaInst v : values) {
                BasicBlock entry = f.getBlockArrayList().get(0);
                Stack<Value> reachingDef = new Stack<>();
                entry.preOrderForRename(v, reachingDef);
            }
        }
    }
}
