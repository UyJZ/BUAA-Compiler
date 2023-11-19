package llvm_ir.llvmType;

public class Integer32Type extends LLVMType {

    public Integer32Type() {
        len = 4;
    }

    @Override
    public String toString() {
        return "i32";
    }
}
