package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

import java.util.HashMap;

public class UndefinedVal extends Value {
    public UndefinedVal() {
        super(new Integer32Type(), "0");
    }

    @Override
    public Value copy(HashMap<Value, Value> map) {
        return new UndefinedVal();
    }
}
