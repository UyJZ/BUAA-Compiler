package MidEnd;

import FrontEnd.Nodes.Func.MainFuncDef;
import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class DeadCodeDeletion {

    private final Module module;

    public DeadCodeDeletion(Module module) {
        this.module = module;
    }

    public void run() {
        deleteDeadFunc();
        deleteDeadCode();
    }

    private void deleteDeadFunc() {
        Function mainFunc = null;
        for (Function function : module.getFunctionList()) {
            if (function.isMainFunc()) {
                mainFunc = function;
            }
        }
        if (mainFunc == null) {
            throw new RuntimeException("No main function");
        }
        LinkedHashSet<Function> funcToDelete = new LinkedHashSet<>();
        for (Function function : module.getFunctionList()) {
            if (function != mainFunc && !function.isSysCall() && !mainFunc.getCallFunctions().contains(function)) {
                funcToDelete.add(function);
            }
        }
        for (Function function : funcToDelete) {
            module.getFunctionList().remove(function);
        }
    }

    private void deleteDeadCode() {
        for (Function function : module.getFunctionList()) {
            HashSet<Instr> deadInstrSet = new HashSet<>();
            HashSet<Instr> records = new HashSet<>();
            for (BasicBlock block : function.getBlockArrayList()) {
                for (Instr instr : block.getInstrs()) {
                    if (instr.canBeDeleted(deadInstrSet, records)) {
                        deadInstrSet.add(instr);
                    }
                }
            }
            for (BasicBlock block : function.getBlockArrayList()) {
                ArrayList<Instr> instrs = block.getInstrs();
                instrs.removeIf(instr -> deadInstrSet.contains(instr));
            }
        }
    }
}
