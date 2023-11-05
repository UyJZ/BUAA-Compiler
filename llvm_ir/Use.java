package llvm_ir;

import llvm_ir.llvmType.LLVMType;

public class Use{
    private User user;

    private Value value;

    public Use(User user, Value value) {
        this.user = user;
        this.value = value;
    }
}
