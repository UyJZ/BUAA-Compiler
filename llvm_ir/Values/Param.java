package llvm_ir.Values;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class Param extends Value {
    public Param(LLVMType type) {
        super(type, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
    }

    public Param(LLVMType type, String name) { // for const string
        super(type, name);
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }
}
