package IR_LLVM.LLVM_Values;

import IR_LLVM.LLVM_Value;

import java.util.HashMap;

public class UndefinedVal extends ConstInteger {
    public UndefinedVal() {
        super(0);
    }

    @Override
    public LLVM_Value copy(HashMap<LLVM_Value, LLVM_Value> map) {
        return new UndefinedVal();
    }
}
