package IR_LLVM.LLVM_Values;

import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Types.LLVMType;

public class TempValue extends LLVM_Value {
    public TempValue(LLVMType type) {
        super(type, "");
    }
}
