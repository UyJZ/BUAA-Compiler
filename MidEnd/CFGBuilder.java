package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class CFGBuilder {

    private final Module module;

    private HashMap<BasicBlock, LinkedHashSet<BasicBlock>> posCFG;

    private HashMap<BasicBlock, LinkedHashSet<BasicBlock>> preCFG;

    private HashMap<BasicBlock, LinkedHashSet<BasicBlock>> DomMap;

    private HashMap<BasicBlock, BasicBlock> immDomMap;

    public CFGBuilder(Module module) {
        this.module = module;
        posCFG = new HashMap<>();
        preCFG = new HashMap<>();
        DomMap = new HashMap<>();
        immDomMap = new HashMap<>();
    }

    public void run() {
        buildCFG();
        buildDominateRel();
    }

    private void buildCFG() {
        for (Function f : module.getFunctionList()) {
            for (BasicBlock block : f.getBlockArrayList()) {
                posCFG.put(block, new LinkedHashSet<>(block.getPosBlocks()));
                preCFG.put(block, new LinkedHashSet<>(block.getPreBlocks()));
            }
        }
    }

    private void buildDominateRel() {
        //build strict dominate
        for (Function f : module.getFunctionList()) {
            if (f.getBlockArrayList().size() == 0) continue;
            BasicBlock entry = f.getBlockArrayList().get(0);
            while (true) {
                boolean allReachAble = true;
                ArrayList<BasicBlock> UnReachAbleBlock = new ArrayList<>();
                Iterator<BasicBlock> iterator = f.getBlockArrayList().iterator();
                while (iterator.hasNext()) {
                    BasicBlock block = iterator.next();
                    if (block.isReachAble()) {
                        if (block == entry) continue;
                        ArrayList<ArrayList<BasicBlock>> roads = new ArrayList<>();
                        ArrayList<BasicBlock> road = new ArrayList<>();
                        entry.DFSBuildStrictDominator(block, road, roads);
                        LinkedHashSet<BasicBlock> blocks = null;
                        for (ArrayList<BasicBlock> road1 : roads) {
                            if (blocks == null) {
                                blocks = new LinkedHashSet<>(road1);
                            } else {
                                blocks.retainAll(road1);
                            }
                        }
                        if (blocks == null) {
                            allReachAble = false;
                            UnReachAbleBlock.add(block);
                            iterator.remove();
                            continue;
                        }
                        block.setStrictDominator(blocks);
                        block.setDominators(blocks);
                        block.buildImmDominator();
                        if (block.getImmDominator() != null)
                            immDomMap.put(block, block.getImmDominator());
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
}
