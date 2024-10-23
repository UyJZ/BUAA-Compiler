package Ir_LLVM.LLVM_Types;

public class BoolType extends LLVMType{

    public BoolType() {
        len = 4;
    }

    @Override
    public String toString() {
        return "i1";
    }
}
