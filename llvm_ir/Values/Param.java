package llvm_ir.Values;

import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class Param extends Value {
    public Param(LLVMType type) {
        super(type, IRController.getInstance().genVirtualRegNum());
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }
}
