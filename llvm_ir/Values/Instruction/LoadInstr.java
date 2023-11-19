package llvm_ir.Values.Instruction;

import Config.tasks;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

public class LoadInstr extends Instr {

    private Value ptr;

    public LoadInstr(Value ptr) {
        super(((PointerType) ptr.getType()).getElementType(), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.ptr = ptr;
        this.operands.add(ptr);
    }

    @Override
    public String toString() {
        return name + " = load " + type + " , " + type + "* " + ptr.getName();
    }

}
