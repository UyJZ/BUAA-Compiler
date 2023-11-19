package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class ZextInstr extends Instr {

    private LLVMType type1;

    private Value operand;

    public ZextInstr(LLVMType type1, LLVMType type2, Value operand) {
        super(type2, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.operand = operand;
        this.operands.add(operand);
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return name + " = " + "zext " + type1.toString() + " " + operand.getName() + " to " + type.toString();
    }
}
