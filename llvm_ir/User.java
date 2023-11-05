package llvm_ir;

import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class User extends Value {
    protected ArrayList<Value> operands;

    public User(LLVMType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
    }
}
