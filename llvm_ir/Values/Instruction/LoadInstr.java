package llvm_ir.Values.Instruction;

import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class LoadInstr extends Instr {

    private String ptr;

    public LoadInstr(LLVMType type, Value ptr) {
        super(type, IRController.getInstance().genVirtualRegNum());
        this.ptr = ptr.getName();
        this.operands.add(ptr);
    }

    @Override
    public String toString() {
        return name + " = load " + type + " , " + type + "* " + ptr;
    }

}
