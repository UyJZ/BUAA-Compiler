package llvm_ir;

import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class User extends Value {
    protected ArrayList<Value> operands;

    public User(LLVMType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
    }

    protected void addValue(Value value) {
        this.operands.add(value);
        IRController.getInstance().addUse(new Use(this, value));
    }

    public ArrayList<Value> getOperands() {
        return this.operands;
    }

    public void replaceValue(Value old, Value new_) {
        for (int i = 0; i < this.operands.size(); i++) {
            if (this.operands.get(i) == old) {
                this.operands.set(i, new_);
                IRController.getInstance().replaceUse(this, old, new_);
            }
        }
    }
}
