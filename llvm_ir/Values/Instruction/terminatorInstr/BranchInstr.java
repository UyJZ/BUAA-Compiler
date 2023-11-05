package llvm_ir.Values.Instruction.terminatorInstr;

import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.LLVMType;

public class BranchInstr extends Instr {
    public BranchInstr(LLVMType type, String name) {
        super(type, name);
    }
}
