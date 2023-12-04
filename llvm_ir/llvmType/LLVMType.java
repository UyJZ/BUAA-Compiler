package llvm_ir.llvmType;

public class LLVMType {

    protected int len = 0;

    protected boolean hasElement = this instanceof PointerType || this instanceof ArrayType;

    private boolean isConst = false;

    public int getLen() {
        return len;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    public void setConst() {
        isConst = true;
    }

    public boolean isConst() {
        return isConst;
    }

    public boolean isHasElement() {
        return hasElement;
    }
}
