package llvm_ir.llvmType;

public class VoidType extends LLVMType{

    public static final VoidType voidType = new VoidType();

    @Override
    public String toString() {
        return "void";
    }
}
