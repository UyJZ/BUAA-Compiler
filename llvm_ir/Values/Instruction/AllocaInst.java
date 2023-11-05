package llvm_ir.Values.Instruction;

import llvm_ir.llvmType.LLVMType;

public class AllocaInst extends Instr{
    public AllocaInst(LLVMType type, String name) {
        super(type, name);
    }

    @Override
    public String toString() {
        return name + " = alloca " + type.toString();
    }
}
