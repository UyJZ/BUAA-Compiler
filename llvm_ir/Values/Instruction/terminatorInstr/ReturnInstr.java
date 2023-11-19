package llvm_ir.Values.Instruction.terminatorInstr;

import llvm_ir.Value;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

public class ReturnInstr extends Instr {
    public ReturnInstr(LLVMType type) {
        super(type, "");
    }

    public ReturnInstr(LLVMType type, Value value) {
        super(type, value.getName());
        this.operands.add(value);
    }

    @Override
    public String toString() {
        if (type instanceof VoidType) {
            return "ret " + type.toString();
        } else {
            return "ret " + type.toString() + " " + name;
        }
    }

    public void setName() {
        if (type instanceof VoidType) {
            name = "";
        } else {
            name = operands.get(0).getName();
        }
    }
}
