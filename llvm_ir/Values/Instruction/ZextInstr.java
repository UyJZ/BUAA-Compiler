package llvm_ir.Values.Instruction;

import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class ZextInstr extends Instr {

    private LLVMType type1;

    private String operand;

    public ZextInstr(LLVMType type1, LLVMType type2, Value operand) {
        super(type2, IRController.getInstance().genVirtualRegNum());
        this.operand = operand.getName();
        this.operands.add(operand);
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return name + " = " + "zext " + type1.toString() + " " + operand + " to " + type.toString();
    }
}
