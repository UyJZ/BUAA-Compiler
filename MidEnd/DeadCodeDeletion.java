package MidEnd;

import Ir_LLVM.LLVM_Module;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Function;
import Ir_LLVM.LLVM_Values.Instr.Instr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class DeadCodeDeletion {

    private final LLVM_Module LLVMModule;

    public DeadCodeDeletion(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
    }

    public void run() {
        deleteDeadFunc();
        deleteDeadCode();
    }

    private void deleteDeadFunc() {
        Function mainFunc = null;
        for (Function function : LLVMModule.getFunctionList()) {
            if (function.isMainFunc()) {
                mainFunc = function;
            }
        }
        if (mainFunc == null) {
            throw new RuntimeException("No main function");
        }
        LinkedHashSet<Function> funcToDelete = new LinkedHashSet<>();
        for (Function function : LLVMModule.getFunctionList()) {
            if (function != mainFunc && !function.isSysCall() && !mainFunc.getCallFunctions().contains(function)) {
                funcToDelete.add(function);
            }
        }
        for (Function function : funcToDelete) {
            LLVMModule.getFunctionList().remove(function);
        }
    }

    private void deleteDeadCode() {
        for (Function function : LLVMModule.getFunctionList()) {
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
