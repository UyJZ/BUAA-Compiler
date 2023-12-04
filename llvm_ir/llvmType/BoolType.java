package llvm_ir.llvmType;

public class BoolType extends LLVMType{

    public BoolType() {
        len = 4;
    }

    @Override
    public String toString() {
        return "i1";
    }
}
