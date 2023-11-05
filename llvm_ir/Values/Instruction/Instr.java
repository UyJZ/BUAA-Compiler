package llvm_ir.Values.Instruction;

import llvm_ir.User;
import llvm_ir.llvmType.LLVMType;

public class Instr extends User {
    public Instr(LLVMType type, String name) {
        super(type, name);
    }
}
