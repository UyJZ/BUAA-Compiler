package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.BoolType;

public class ConstBool extends Value {
    private final boolean val;

    public ConstBool(boolean n) {
        super(new BoolType(), n ? "1" : "0");
        val = n;
    }

    public boolean isTrue() {
        return val;
    }
}
