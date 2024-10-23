package IR_LLVM;

public class LLVM_Use {
    private LLVM_User LLVMUser;

    private LLVM_Value LLVMValue;

    public LLVM_Use(LLVM_User LLVMUser, LLVM_Value LLVMValue) {
        this.LLVMUser = LLVMUser;
        this.LLVMValue = LLVMValue;
    }

    public LLVM_User getUser() {
        return this.LLVMUser;
    }

    public LLVM_Value getValue() {
        return this.LLVMValue;
    }

    public void setValue(LLVM_Value LLVMValue) {
        this.LLVMValue = LLVMValue;
    }
}
