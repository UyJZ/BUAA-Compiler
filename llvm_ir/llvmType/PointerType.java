package llvm_ir.llvmType;

public class PointerType extends LLVMType{
    private LLVMType elementType;

    public PointerType(LLVMType elementType) {
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return elementType.toString() + "*";
    }

    public LLVMType getElementType() {
        return elementType;
    }
}
