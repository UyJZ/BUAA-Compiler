package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Function;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;

public class CallInstr extends Instr {

    private final ArrayList<Value> params;

    private Function function;

    private final String functionName;

    private final boolean isIOInstr;

    private final boolean isInputInstr;

    private final boolean isOutputInstr;

    public CallInstr(LLVMType type, Value func, ArrayList<Value> params) {
        //库函数
        super(type, type instanceof VoidType || tasks.isIsOptimize() ? "" : IRController.getInstance().genVirtualRegNum());
        this.params = params;
        functionName = func.getName();
        isIOInstr = functionName.equals("@getint") || functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        isInputInstr = functionName.equals("@getint");
        isOutputInstr = functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        if (!isIOInstr) this.operands.add(func);
        this.operands.addAll(params);
    }

    @Override
    public String toString() {
        String FuncName = (function == null) ? functionName : function.getName();
        StringBuilder sb = new StringBuilder();
        if (type instanceof VoidType) {
            sb.append("call ").append(type.toString()).append(" ").append(FuncName).append("(");
        } else {
            sb.append(name).append(" = call ").append(type.toString()).append(" ").append(FuncName).append("(");
        }
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getType().toString()).append(" ").append(params.get(i).getName());
            if (i != params.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isInputInstr() {
        return isInputInstr;
    }

    public boolean isOutputInstr() {
        return isOutputInstr;
    }

    public String getFunctionName() {
        return functionName;
    }

    public boolean isIOInstr() {
        return isIOInstr;
    }

    public ArrayList<Value> getParam() {
        return params;
    }

    public void setName() {
        if (type instanceof Integer32Type) this.name = IRController.getInstance().genVirtualRegNum();
    }
}
