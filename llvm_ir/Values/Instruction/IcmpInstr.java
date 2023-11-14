package llvm_ir.Values.Instruction;

import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.BoolType;
import llvm_ir.llvmType.LLVMType;

public class IcmpInstr extends Instr {

    private final String operand1;
    private final String operand2;

    private final LLVMType CompareType;

    public enum CmpOp {
        eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
    }

    private final CmpOp Op;

    public IcmpInstr(LLVMType type, Value operand1, Value operand2, CmpOp cmpOp) {
        super(new BoolType(), IRController.getInstance().genVirtualRegNum());
        this.operand1 = operand1.getName();
        this.operand2 = operand2.getName();
        this.operands.add(operand1);
        this.operands.add(operand2);
        this.Op = cmpOp;
        this.CompareType = type;
    }

    @Override
    public String toString() {
        return name + " = " + "icmp " + Op.toString() + " " + CompareType.toString() + " " + operand1 + ", " + operand2;
    }
}
