package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.ArrayList;
import java.util.HashMap;
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

    private void buildCFG() {
        for (Function f : module.getFunctionList()) {
            for (BasicBlock block : f.getBlockArrayList()) {
                posCFG.put(block, new LinkedHashSet<>(block.getPosBlocks()));
                preCFG.put(block, new LinkedHashSet<>(block.getPreBlocks()));
            }
        }
    }

    private void buildDominateRel() {
        HashMap<BasicBlock, LinkedHashSet<BasicBlock>> tempDominateMap = new HashMap<>();
        do {
            DomMap = tempDominateMap;
            for (Function f : module.getFunctionList()) {
                for (BasicBlock block : f.getBlockArrayList()) {
                    LinkedHashSet<BasicBlock> dominateSet = null;
                    for (BasicBlock block1 : block.getPreBlocks()) {
                        if (dominateSet == null) {
                            dominateSet = new LinkedHashSet<>(block1.getDominatee());
                        } else {
                            dominateSet.retainAll(block1.getDominatee());
                        }
                    }
                    if (dominateSet != null) {
                        dominateSet.add(block);
                        block.setDominatee(dominateSet);
                    }
                    tempDominateMap.put(block, block.getDominatee());
                }
            }
        } while (!tempDominateMap.equals(DomMap));
        //build strict dominate
        for (Function f : module.getFunctionList()) {
            if (f.getBlockArrayList().size() == 0) continue;
            BasicBlock entry = f.getBlockArrayList().get(0);
            for (BasicBlock block : f.getBlockArrayList()) {
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
                    f.deleteBlock(block);
                    continue;
                }
                block.setStrictDominator(blocks);
                block.buildImmDominator();
                if (block.getImmDominator() != null)
                    immDomMap.put(block, block.getImmDominator());
            }
        }
        //build dominate frontier
        for (Function f : module.getFunctionList()) {
            for (BasicBlock b : f.getBlockArrayList()) {
                for (BasicBlock b1 : b.getPosBlocks()) {
                    BasicBlock x = b;
                    BasicBlock y = b1;
                    while(!y.isStrictDominator(x)) {
                        LinkedHashSet<BasicBlock> set = x.getDominateFrontier();
                        set.add(y);
                        x = x.getImmDominator();
                    }
                }
            }
        }

    }


}
