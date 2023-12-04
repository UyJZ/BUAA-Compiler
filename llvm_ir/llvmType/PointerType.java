package llvm_ir.llvmType;

public class PointerType extends LLVMType{
    private LLVMType elementType;

    public PointerType(LLVMType elementType) {
        this.elementType = elementType;
        this.len = 4;
    }

    @Override
    public String toString() {
        return elementType.toString() + "*";
    }

    public LLVMType getElementType() {
        return elementType;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
