package llvm_ir;

import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.Values.Instruction.PhiInstr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class User extends Value {
    protected ArrayList<Value> operands;

    protected ArrayList<Use> useList;

    public User(LLVMType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
        this.useList = new ArrayList<>();
    }

    protected void addValue(Value value) {
        this.operands.add(value);
        useList.add(new Use(this, value));
        value.addUsedBy(this);
    }

    protected void setValue(int index, Value value) {
        this.operands.set(index, value);
        useList.set(index, new Use(this, value));
        value.addUsedBy(this);
    }


    public ArrayList<Value> getOperands() {
        return this.operands;
    }

    public boolean replaceValue(Value old, Value new_) {
        boolean flag = false;
        for (int i = 0; i < this.operands.size(); i++) {
            if (this.operands.get(i) == old) {
                this.operands.set(i, new_);
                flag = true;
            }
        }
        return flag;
    }
}
