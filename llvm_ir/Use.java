package llvm_ir;

import llvm_ir.llvmType.LLVMType;

public class Use{
    private User user;

    private Value value;

    public Use(User user, Value value) {
        this.user = user;
        this.value = value;
    }

    public User getUser() {
        return this.user;
    }

    public Value getValue() {
        return this.value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
