package llvm_ir;

import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.LLVMType;

import java.io.PrintStream;
import java.util.HashMap;

public class IRController {

    private static IRController instance = new IRController();

    private HashMap<Function, Integer> virtualRegNumMap;

    private Function currentFunction;

    private BasicBlock currentBasicBlock;

    private Model model;

    private IRController() {
        virtualRegNumMap = new HashMap<>();
        model = new Model();
    }

    public static IRController getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        virtualRegNumMap.put(function, 0);
        model.addFunction(function);
        currentFunction = function;
    }

    public void addInstr(Instr instr) {
        currentBasicBlock.addInstr(instr);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        model.addGlobalVar(globalVar);
    }

    public void addNewBasicBlock(BasicBlock block) {
        currentFunction.addBasicBlock(block);
        currentBasicBlock = block;
    }

    public String genVirtualRegNum() {
        int num = virtualRegNumMap.get(currentFunction);
        virtualRegNumMap.put(currentFunction, num + 1);
        if (num == 1 && currentFunction.getName().equals("@main")) {
            System.out.println("debug");
        }
        return "%" + num;
    }


    public void addParam(VarSymbol symbol) {
        LLVMType type = symbol.getType();
        Param param = new Param(type);
        symbol.setLlvmValue(param);
        currentFunction.addParam(param);
    }

    public void Output(PrintStream ps) {
        ps.println(model);
    }

    public Function getFunctionByName(String name) {
        String N = "@" + name;
        for (Function function : virtualRegNumMap.keySet()) {
            if (function.getName().equals(N)) return function;
        }
        return null;
    }
}
