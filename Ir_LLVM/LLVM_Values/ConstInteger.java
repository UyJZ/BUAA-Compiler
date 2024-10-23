package Ir_LLVM.LLVM_Values;

import BackEnd.MIPS.Register;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Types.Integer32Type;

import java.util.HashMap;

public class ConstInteger extends LLVM_Value {

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
    public LLVM_Value copy(HashMap<LLVM_Value, LLVM_Value> map) {
        return new ConstInteger(val);
    }

    @Override
    public boolean isDistributable() {
        return false;
    }
}
