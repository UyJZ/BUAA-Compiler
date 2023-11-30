package llvm_ir.Values;

import BackEnd.MIPS.Register;
import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

import java.util.HashMap;

public class ConstInteger extends Value {

    private final int val;
    public ConstInteger(int n) {
        super(new Integer32Type(), String.valueOf(n));
        val = n;
        if (n == 0) {
            useReg = true;
            isDistributed = true;
            register = Register.ZERO;
        }
    }

    public int getVal() {
        return val;
    }

    @Override
    public Value copy(HashMap<Value, Value> map) {
        return new ConstInteger(val);
    }
}
