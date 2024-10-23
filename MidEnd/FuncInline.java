package MidEnd;

import IR_LLVM.LLVM_Module;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.InlinedFunc;
import IR_LLVM.LLVM_Values.Instr.CallInstr;
import IR_LLVM.LLVM_Values.Instr.Instr;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class FuncInline {
    private final LLVM_Module LLVMModule;

    public FuncInline(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
    }

    public void run() {
        functionInline();
    }

    public void functionInline() {
        LinkedHashSet<Function> inlineFunc = new LinkedHashSet<>();
        for (Function f : LLVMModule.getFunctionList()) {
            if (f.isSysCall() || f.isMainFunc()) {
                continue;
            }
            for (BasicBlock block : f.getBlockArrayList()) {
                if (!f.isInlineAble()) {
                    break;
                }
                for (Instr instr : block.getInstrs()) {
                    if (instr instanceof CallInstr callInstr && callInstr.getFunction() == f) {
                        f.setRecursive();
                        f.setInlineAble(false);
                        break;
                    } else if (instr instanceof CallInstr callInstr && callInstr.getFunction().isRecursive()) {
                        f.setRecursive();
                        f.setInlineAble(false);
                        break;
                    }
                }
            }
            if (f.isInlineAble()) {
                inlineFunc.add(f);
            }
        }
        Iterator<Function> iterator = LLVMModule.getFunctionList().iterator();
        while (iterator.hasNext()) {
            Function f = iterator.next();
            if (inlineFunc.contains(f)) {
                iterator.remove();
            } else {
                for (int i = 0; i < f.getBlockArrayList().size(); i++) {
                    BasicBlock currentBlock = f.getBlockArrayList().get(i);
                    for (int j = 0;j < currentBlock.getInstrs().size(); j++) {
                        ArrayList<Instr> instrs = currentBlock.getInstrs();
                        if (instrs.get(j) instanceof CallInstr callInstr && inlineFunc.contains(callInstr.getFunction())) {
                            BasicBlock nextBlock = new BasicBlock();
                            InlinedFunc func = callInstr.getFunction().inline(callInstr.getParam(), nextBlock, new HashMap<>());
                            nextBlock.addInstr(func.getPhi());
                            callInstr.replacedBy(func.getPhi());
                            for (int k = j + 1; k < instrs.size(); k++) {
                                nextBlock.addInstr(instrs.get(k));
                            }
                            for (int t = 1;t < nextBlock.getInstrs().size(); t++) {
                                currentBlock.removeInstr(nextBlock.getInstrs().get(t));
                            }
                            currentBlock.removeInstr(instrs.get(j));
                            currentBlock.addInstr(new BranchInstr(func.getBlocks().get(0)));
                            for (int t = 0; t < func.getBlocks().size(); t++) {
                                f.addBasicBlock(i + t + 1, func.getBlocks().get(t));
                            }
                            f.addBasicBlock(i + func.getBlocks().size() + 1, nextBlock);
                            i = i + func.getBlocks().size() - 1;
                            currentBlock.replacedByInPhi(nextBlock);
                            break;
                        }
                    }
                }
            }
        }
    }
}
