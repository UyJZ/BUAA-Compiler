package Ir_LLVM.LLVM_Values;

import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Types.BoolType;

import java.util.HashMap;

public class ConstBool extends ConstInteger {
    private final boolean val;

    public ConstBool(boolean n) {
        //super(new BoolType(), n ? "1" : "0");
        super(n ? 1 : 0);
        this.type = new BoolType();
        val = n;
    }

    public boolean isTrue() {
        return val;
    }

    public int getVal() {
        return val ? 1 : 0;
    }

    @Override
    public LLVM_Value copy(HashMap<LLVM_Value, LLVM_Value> map) {
        return new ConstBool(val);
    }
}
