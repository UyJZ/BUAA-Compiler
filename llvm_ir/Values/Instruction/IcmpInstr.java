package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.BoolType;
import llvm_ir.llvmType.LLVMType;

public class IcmpInstr extends Instr {

    private final Value operand1;
    private final Value operand2;

    public enum CmpOp {
        eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
    }

    private final CmpOp Op;

    public IcmpInstr(Value operand1, Value operand2, CmpOp cmpOp) {
        super(new BoolType(),  tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operands.add(operand1);
        this.operands.add(operand2);
        this.Op = cmpOp;
    }

    @Override
    public String toString() {
        return name + " = " + "icmp " + Op.toString() + " " + operand1.getType().toString() + " " + operand1.getName() + ", " + operand2.getName();
    }
}
