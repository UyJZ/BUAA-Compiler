package Ir_LLVM.LLVM_Types;

public class Integer8Type extends LLVMType {

    public Integer8Type() {
        len = 4;
    }

    @Override
    public String toString() {
        return "i8";
    }
}
