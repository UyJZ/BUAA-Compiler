package IR_LLVM;

import Config.Tasks;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.GlobalVar;
import IR_LLVM.LLVM_Values.Instr.Instr;
import IR_LLVM.LLVM_Values.Param;
import IR_LLVM.LLVM_Types.LLVMType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LLVM_Builder {

    private static LLVM_Builder instance = new LLVM_Builder();

    private HashMap<Function, Integer> virtualRegNumMap;

    private ArrayList<LLVM_Use> LLVMUseList;

    private Function currentFunction;

    private BasicBlock currentBasicBlock;

    private LLVM_Module LLVMModule;

    private LLVM_Builder() {
        virtualRegNumMap = new HashMap<>();
        LLVMModule = new LLVM_Module();
        LLVMUseList = new ArrayList<>();
    }

    public void addUse(LLVM_Use LLVMUse) {
        LLVMUseList.add(LLVMUse);
    }

    public void replaceUse(LLVM_User LLVMUser, LLVM_Value old, LLVM_Value new_) {
        for (LLVM_Use LLVMUse : LLVMUseList) {
            if (LLVMUse.getUser() == LLVMUser && LLVMUse.getValue() == old) {
                LLVMUse.setValue(new_);
            }
        }
    }

    public static LLVM_Builder getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        virtualRegNumMap.put(function, 0);
        LLVMModule.addFunction(function);
        currentFunction = function;
    }

    public LLVM_Module getModule() {
        return LLVMModule;
    }

    public void addInstr(Instr instr) {
        currentBasicBlock.addInstr(instr);
    }

    public Function getCurrentFunction() {
        return currentFunction;
    }

    public void addGlobalVar(GlobalVar globalVar) {
        LLVMModule.addGlobalVar(globalVar);
    }

    public void addNewBasicBlock(BasicBlock block) {
        block.setName(Tasks.isSetNameAfterGen ? "" : genVirtualRegNum());
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
        ps.println(LLVMModule);
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
        LLVMModule.setName();
    }

    public void flush() {
        for (Map.Entry<Function, Integer> e : virtualRegNumMap.entrySet()) {
            e.setValue(0);
        }
    }
}
