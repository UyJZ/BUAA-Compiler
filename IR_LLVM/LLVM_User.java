package IR_LLVM;

import IR_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;

public class LLVM_User extends LLVM_Value {
    protected ArrayList<LLVM_Value> operands;

    protected ArrayList<LLVM_Use> LLVMUseList;

    public LLVM_User(LLVMType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
        this.LLVMUseList = new ArrayList<>();
    }

    protected void addValue(LLVM_Value LLVMValue) {
        this.operands.add(LLVMValue);
        LLVMUseList.add(new LLVM_Use(this, LLVMValue));
        LLVMValue.addUsedBy(this);
    }

    protected void setValue(int index, LLVM_Value LLVMValue) {
        this.operands.set(index, LLVMValue);
        LLVMUseList.set(index, new LLVM_Use(this, LLVMValue));
        LLVMValue.addUsedBy(this);
    }


    public ArrayList<LLVM_Value> getOperands() {
        return this.operands;
    }

    public boolean replaceValue(LLVM_Value old, LLVM_Value new_) {
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
