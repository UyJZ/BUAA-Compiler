package Ir_LLVM.LLVM_Values;

import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Types.LLVMType;

public class TempValue extends LLVM_Value {
    public TempValue(LLVMType type) {
        super(type, "");
    }
}
