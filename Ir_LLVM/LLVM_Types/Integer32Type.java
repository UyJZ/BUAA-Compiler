package Ir_LLVM.LLVM_Types;

public class Integer32Type extends LLVMType {

    public Integer32Type() {
        len = 4;
    }

    @Override
    public String toString() {
        return "i32";
    }
}
