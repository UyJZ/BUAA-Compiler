package llvm_ir.Values.Instruction;

import llvm_ir.llvmType.LLVMType;

public class PhiInstr extends Instr{
    public PhiInstr(LLVMType type, String name) {
        super(type, name);
    }
}
