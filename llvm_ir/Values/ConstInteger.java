package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

public class ConstInteger extends Value {
    public ConstInteger(int n) {
        super(new Integer32Type(), String.valueOf(n));
    }
}
