package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.InlinedFunc;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class FuncInline {
    private final Module module;

    public FuncInline(Module module) {
        this.module = module;
    }

    public void run() {
        functionInline();
    }

    public void functionInline() {
        LinkedHashSet<Function> inlineFunc = new LinkedHashSet<>();
        for (Function f : module.getFunctionList()) {
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
        Iterator<Function> iterator = module.getFunctionList().iterator();
        while (iterator.hasNext()) {
            Function f = iterator.next();
            if (inlineFunc.contains(f)) {
                iterator.remove();
            } else {
                for (int i = 0; i < f.getBlockArrayList().size(); i++) {
                    BasicBlock currentBlock = f.getBlockArrayList().get(i);
                    ArrayList<Instr> instrs = currentBlock.getInstrs();
                    for (int j = 0;j < instrs.size(); j++) {
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
                                f.addBasicBlock(i + t, func.getBlocks().get(t));
                            }
                            f.addBasicBlock(i + func.getBlocks().size(), nextBlock);
                            i = i + func.getBlocks().size() - 1;
                            break;
                        }
                    }
                }
            }
        }
    }
}
