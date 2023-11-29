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
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;

import java.util.ArrayList;
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
            ArrayList<AllocaInst> values = function.getAllAlloc();
            LinkedHashSet<Value> usefulInstr = new LinkedHashSet<>();
            for (BasicBlock block : function.getBlockArrayList()) {
                for (Instr instr : block.getInstrs()) {
                    if (instr instanceof CallInstr || instr instanceof BranchInstr || instr instanceof ReturnInstr) {
                        instr.DFSForUseful(usefulInstr);
                    }
                }
            }
            for (AllocaInst value : values) {
                value.DFSForUseful(usefulInstr);
            }
            ArrayList<GlobalVar> globalVars = module.getGlobalVarList();
            for (GlobalVar globalVar : globalVars) {
                globalVar.DFSForUseful(usefulInstr);
            }
            for (BasicBlock block : function.getBlockArrayList()) {
                ArrayList<Instr> instrs = block.getInstrs();
                instrs.removeIf(instr -> !usefulInstr.contains(instr));
            }
        }
    }
}
