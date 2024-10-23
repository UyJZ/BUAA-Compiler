package MidEnd;

import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Module;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.Instr.Instr;
import IR_LLVM.LLVM_Values.Instr.MoveInstr;
import IR_LLVM.LLVM_Values.Instr.PcopyInstr;
import IR_LLVM.LLVM_Values.Instr.PhiInstr;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;
import IR_LLVM.LLVM_Values.TempValue;

import java.util.*;

public class De_SSA {

    private LLVM_Module LLVMModule;

    public De_SSA(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
    }

    public void run() {
        SSA_Destruction();
    }

    private void SSA_Destruction() {
        for (Function function : LLVMModule.getFunctionList()) {
            SSA_Phi2Pcopy(function);
            SSA_Pcopy2Move(function);
        }
    }

    private void SSA_Phi2Pcopy(Function function) {
        int len = function.getBlockArrayList().size();
        ArrayList<BasicBlock> bList = new ArrayList<>(function.getBlockArrayList());
        for (BasicBlock block : bList) {
            //主要是为了不处理消除关键边之后产生的基本块
            if (block.getInstrs().get(0) instanceof PhiInstr) {
                LinkedHashMap<BasicBlock, PcopyInstr> map = new LinkedHashMap<>();
                // TODO
                ArrayList<BasicBlock> pre = new ArrayList<>(block.getPreBlocks());
                Iterator<BasicBlock> it = pre.iterator();
                while (it.hasNext()) {
                    PcopyInstr pc_i = new PcopyInstr();
                    BasicBlock preBlock = it.next();
                    map.put(preBlock, pc_i);
                    if (preBlock.getPosBlocks().size() == 1) {
                        //INSERT pc_i TO preBlock
                        preBlock.getInstrs().add(preBlock.getInstrs().indexOf(preBlock.lastInstr()), pc_i);//插到branch前面
                    } else {
                        //INSERT pc_i TO newBlock
                        BasicBlock midBlock = new BasicBlock();
                        BranchInstr last = (BranchInstr) preBlock.lastInstr();
                        if (last.getSuccessors().size() == 1) {
                            preBlock.removeInstr(last);
                            preBlock.addInstr(new BranchInstr(midBlock));
                            midBlock.addInstr(pc_i);
                            midBlock.addInstr(new BranchInstr(block));
                        } else {
                            ArrayList<BasicBlock> newBlocks = new ArrayList<>();
                            for (BasicBlock block1 : last.getSuccessors()) {
                                if (block1 != block) {
                                    newBlocks.add(block1);
                                } else {
                                    newBlocks.add(midBlock);
                                }
                            }
                            preBlock.removeInstr(last);
                            BranchInstr b = new BranchInstr(newBlocks.get(0), newBlocks.get(1), last.getJudge());
                            preBlock.addInstr(b);
                            midBlock.addInstr(pc_i);
                            midBlock.addInstr(new BranchInstr(block));
                        }
                        function.getBlockArrayList().add(function.getBlockArrayList().indexOf(preBlock) + 1, midBlock);
                        midBlock.setFather(function);
                        it.remove();
                    }
                }
                Iterator<Instr> it1 = block.getInstrs().iterator();
                while (it1.hasNext()) {
                    Instr instr = it1.next();
                    if (instr instanceof PhiInstr phiInstr) {
                        for (BasicBlock block1 : phiInstr.getLabels()) {
                            if (map.containsKey(block1)) {
                                map.get(block1).addCopy(phiInstr, phiInstr.getValues().get(phiInstr.getLabels().indexOf(block1)));
                            }
                        }
                        it1.remove();
                    }
                }
            }
        }
    }

    private void SSA_Pcopy2Move(Function function) {
        for (BasicBlock block : function.getBlockArrayList()) {
            for (int k = 0; k < block.getInstrs().size(); k++) {
                Instr instr = block.getInstrs().get(k);
                if (instr instanceof PcopyInstr pcopyInstr) {
                    ArrayList<MoveInstr> seq = new ArrayList<>();
                    while (pcopyInstr.size() > 0) {
                        for (int i = 0; i < pcopyInstr.size(); i++) {
                            LLVM_Value dst = pcopyInstr.getDstList().get(i);
                            if (pcopyInstr.getDstOf(dst) == null) {
                                seq.add(new MoveInstr(dst, pcopyInstr.getSrcOf(dst)));
                                pcopyInstr.removeCopy(dst);
                            } else {
                                TempValue temp = new TempValue(dst.getType());
                                seq.add(new MoveInstr(temp, pcopyInstr.getSrcOf(dst)));
                                pcopyInstr.replaceCopy(dst, pcopyInstr.getSrcOf(dst), temp);
                            }
                        }
                    }
                    //解决寄存器冲突
                    HashMap<Register, LLVM_Value> conflictMap = new HashMap<>();
                    for (int i = seq.size() - 1; i >= 0; i--) {
                        block.getInstrs().add(k, seq.get(i));
                    }
                    block.getInstrs().remove(k + seq.size());
                }
            }
        }
    }

    private boolean hasRegConflict(BasicBlock preBlock, BasicBlock posBlock) {
        ArrayList<PhiInstr> phis = new ArrayList<>();
        for (Instr instr : posBlock.getInstrs()) {
            if (instr instanceof PhiInstr phiInstr) {
                phis.add(phiInstr);
            }
        }
        HashSet<LLVM_Value> set = new HashSet<>();
        return false;
    }

}
