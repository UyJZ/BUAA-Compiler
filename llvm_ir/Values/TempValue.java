package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

import java.lang.reflect.Type;

public class TempValue extends Value {
    public TempValue(LLVMType type) {
        super(type, "");
    }
}
