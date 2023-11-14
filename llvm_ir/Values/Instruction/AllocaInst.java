package llvm_ir.Values.Instruction;

import llvm_ir.IRController;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

public class AllocaInst extends Instr {
    public AllocaInst(LLVMType type) {
        super(new PointerType(type), IRController.getInstance().genVirtualRegNum());
    }

    @Override
    public String toString() {
        return name + " = alloca " + ((PointerType) type).getElementType();
    }
}
