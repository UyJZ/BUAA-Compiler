package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

public class UndefinedVal extends Value {
    public UndefinedVal() {
        super(new Integer32Type(), "0");
    }
}
