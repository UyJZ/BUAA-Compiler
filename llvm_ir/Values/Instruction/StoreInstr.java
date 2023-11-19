package llvm_ir.Values.Instruction;

import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

public class StoreInstr extends Instr {

    private final boolean isStoreVal;

    private int val;

    private Value to;

    private LLVMType type2;

    private Value from;

    public StoreInstr(Value from, Value to) {
        super(new VoidType(), "");
        isStoreVal = false;
        this.from = from;
        this.to = to;
        this.operands.add(from);
        this.operands.add(to);
    }

    @Override
    public String toString() {
        if (isStoreVal) {
            return "store " + from.getType().toString() + " " + val + ", " + to.getType().toString() + " " + to.getName();
        } else {
            return "store " + from.getType().toString() + " " + from.getName() + ", " + to.getType().toString() + " " + to.getName();
        }
    }
}
