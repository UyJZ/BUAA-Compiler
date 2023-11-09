package llvm_ir.llvmType;

public class LLVMType {
    public int getLen() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }
}
