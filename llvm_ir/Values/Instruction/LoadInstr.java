package llvm_ir.Values.Instruction;

import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.llvmType.LLVMType;

public class LoadInstr extends Instr {

    private String ptr;

    public LoadInstr(LLVMType type, String name) {
        super(type, IRController.getInstance().genVirtualRegNum());
        if (name.charAt(0) == '%') ptr = name;
        else ptr = "@" + name;
    }

    @Override
    public String toString() {
        return name + " = load " + type + " , " + type + "* " + ptr;
    }

}
