package llvm_ir.Values.Instruction;

import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class StoreInstr extends Instr {

    private final boolean isStoreVal;

    private int val;

    private String to;

    private LLVMType type2;

    private String from;

    public StoreInstr(LLVMType type, LLVMType type2, Value from, Value to) {
        super(type, to.getName());
        isStoreVal = false;
        this.from = from.getName();
        this.to = to.getName();
        this.operands.add(from);
        this.operands.add(to);
        this.type2 = type2;
    }

    public StoreInstr(LLVMType type, int val, String to) {
        super(type, to);
        isStoreVal = true;
        this.val = val;
    }

    @Override
    public String toString() {
        if (isStoreVal) {
            return "store " + type.toString() + " " + val + ", " + type2.toString() + " " + name;
        } else {
            return "store " + type.toString() + " " + from + ", " + type2.toString() + " " + name;
        }
    }
}
