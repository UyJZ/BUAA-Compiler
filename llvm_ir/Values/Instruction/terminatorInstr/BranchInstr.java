package llvm_ir.Values.Instruction.terminatorInstr;

import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.BoolType;
import llvm_ir.llvmType.LLVMType;

public class BranchInstr extends Instr {

    private final Value judge;

    private final BasicBlock label1, label2;

    public BranchInstr(LLVMType type, BasicBlock label1, BasicBlock label2, Value judge) {
        super(type, "");
        this.label1 = label1;
        this.label2 = label2;
        this.judge = judge;
    }

    public BranchInstr(LLVMType type, BasicBlock label1) {
        super(type, "");
        this.label1 = label1;
        this.label2 = null;
        this.judge = null;
    }

    @Override
    public String toString() {
        if (label2 == null && judge == null)
            return "br label " + label1.getName();
        return "br " + new BoolType().toString() + " " + judge.getName() + ", label " + label1.getName() + ", label " + label2.getName();
    }
}
