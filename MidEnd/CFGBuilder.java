package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.*;

public class CFGBuilder {

    private final Module module;

    public CFGBuilder(Module module) {
        this.module = module;
    }

    public void run() {
        buildCFG();
        buildDominateRel();
    }

    private void buildCFG() {
        for (Function f : module.getFunctionList()) {
            for (BasicBlock block : f.getBlockArrayList()) {
                block.flush();
            }
            for (BasicBlock block : f.getBlockArrayList()) {
                block.buildPrePos();
            }
        }
    }

    private void buildDominateRel() {
        //build strict dominate
        for (Function f : module.getFunctionList()) {
            if (f.getBlockArrayList().size() == 0) continue;
            BasicBlock entry = f.getBlockArrayList().get(0);
            //删除不可达块
            while (true) {
                boolean allReachAble = true;
                ArrayList<BasicBlock> UnReachAbleBlock = new ArrayList<>();
                Iterator<BasicBlock> iterator = f.getBlockArrayList().iterator();
                while (iterator.hasNext()) {
                    BasicBlock block = iterator.next();
                    if (block.isReachAble()) {
                        LinkedHashSet<BasicBlock> reach = new LinkedHashSet<>();
                        DFSForDom(entry, block, reach);
                        LinkedHashSet<BasicBlock> blocks = new LinkedHashSet<>(f.getBlockArrayList());
                        if (!reach.contains(block)) {
                            allReachAble = false;
                            UnReachAbleBlock.add(block);
                            continue;
                        }
                        for (BasicBlock b : reach) {
                            blocks.remove(b);
                        }
                        block.setDominatee(blocks);
                        block.setStrictDominatee(blocks);
                    }
                }
                if (allReachAble) break;
                else {
                    for (BasicBlock block : UnReachAbleBlock) {
                        block.deleteBlock();
                        f.removeBlock(block);
                    }
                }
            }
            for (BasicBlock block : f.getBlockArrayList()) {
                block.buildImmDominator();
            }
        }
        //build dominate frontier
        for (Function f : module.getFunctionList()) {
            for (BasicBlock b : f.getBlockArrayList()) {
                for (BasicBlock b1 : b.getPosBlocks()) {
                    BasicBlock x = b;
                    BasicBlock y = b1;
                    while (!x.isDominatorOf(y) && x != f.getBlockArrayList().get(0)) {
                        LinkedHashSet<BasicBlock> set = new LinkedHashSet<>(x.getDominateFrontier());
                        set.add(y);
                        x.setDominateFrontier(set);
                        x = x.getImmDominator();
                    }
                }
            }
        }

    }

    private void DFSForDom(BasicBlock cur, BasicBlock tar, LinkedHashSet<BasicBlock> reachedSet) {
        reachedSet.add(cur);
        if (cur.equals(tar)) {
            return;
        }
        for (BasicBlock sucBB : cur.getPosBlocks()) {
            if (! reachedSet.contains(sucBB)) {
                DFSForDom(sucBB, tar, reachedSet);
            }
        }
    }
}
