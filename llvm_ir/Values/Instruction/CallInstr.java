package llvm_ir.Values.Instruction;

import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Function;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import javax.management.ValueExp;
import java.util.ArrayList;

public class CallInstr extends Instr {

    private ArrayList<Value> params;

    private Function function;

    private String functionName;

    public CallInstr(LLVMType type, Value func, ArrayList<Value> params, String targetReg) {
        //库函数
        super(type, type instanceof VoidType ? "" : targetReg);
        this.params = params;
        this.operands.add(func);
        this.operands.addAll(params);
        functionName = func.getName();
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
}
