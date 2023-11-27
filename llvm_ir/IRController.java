package llvm_ir;

import Config.tasks;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.LLVMType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class IRController {

    private static IRController instance = new IRController();

    private HashMap<Function, Integer> virtualRegNumMap;

    private ArrayList<Use> useList;

    private Function currentFunction;

    private BasicBlock currentBasicBlock;

    private Module module;

    private IRController() {
        virtualRegNumMap = new HashMap<>();
        module = new Module();
        useList = new ArrayList<>();
    }

    public void addUse(Use use) {
        useList.add(use);
    }

    public void replaceUse(User user, Value old, Value new_) {
        for (Use use : useList) {
            if (use.getUser() == user && use.getValue() == old) {
                use.setValue(new_);
            }
        }
    }

    public static IRController getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        virtualRegNumMap.put(function, 0);
        module.addFunction(function);
        currentFunction = function;
    }

    public Module getModule() {
        return module;
    }

    public void addInstr(Instr instr) {
        currentBasicBlock.addInstr(instr);
        if (instr instanceof BranchInstr branchInstr) {
            for (BasicBlock block : branchInstr.getSuccessors()) {
                block.addPreBlock(currentBasicBlock);
                currentBasicBlock.addPosBlock(block);
            }
        }
    }

    public Function getCurrentFunction() {
        return currentFunction;
    }

    public void addGlobalVar(GlobalVar globalVar) {
        module.addGlobalVar(globalVar);
    }

    public void addNewBasicBlock(BasicBlock block) {
        block.setName(tasks.isOptimize ? "" : genVirtualRegNum());
        currentFunction.addBasicBlock(block);
        currentBasicBlock = block;
    }

    public String genVirtualRegNum() {
        int num = virtualRegNumMap.get(currentFunction);
        virtualRegNumMap.put(currentFunction, num + 1);
        return "%" + num;
    }


    public void addParam(VarSymbol symbol) {
        LLVMType type = symbol.getType();
        Param param = new Param(type);
        symbol.setLlvmValue(param);
        currentFunction.addParam(param);
    }

    public void Output(PrintStream ps) {
        ps.println(module);
    }

    public Function getFunctionByName(String name) {
        String N = "@" + name;
        for (Function function : virtualRegNumMap.keySet()) {
            if (function.getName().equals(N)) return function;
        }
        return null;
    }

    public void setCurrentFunction(Function function) {
        assert (virtualRegNumMap.containsKey(function));
        currentFunction = function;
    }

    public void setName() {
        module.setName();
    }
}
