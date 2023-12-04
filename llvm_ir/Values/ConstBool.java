package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.BoolType;

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
    public Value copy(HashMap<Value, Value> map) {
        return new ConstBool(val);
    }
}
