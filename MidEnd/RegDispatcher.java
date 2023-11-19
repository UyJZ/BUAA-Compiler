package MidEnd;

import BackEnd.MIPS.Register;
import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.BinaryInstr;
import llvm_ir.llvmType.ArrayType;

import java.util.HashMap;
import java.util.HashSet;

public class RegDispatcher {
    private static final RegDispatcher regDispatcher = new RegDispatcher();

    private Function currentFunction;

    private HashSet<Register> freeRegs;

    private HashSet<Register> usedRegs;

    private HashMap<Function, HashMap<Value, Register>> Val2Reg;

    private HashMap<Function, HashMap<Value, Integer>> Val2Offset;

    private HashMap<Function, HashMap<Register, Value>> Reg2Val;

    private HashMap<Function, Integer> OffsetMap;

    private int currentOffset;

    private Module module;

    public boolean hasFreeReg() {
        return false;
    }

    public Register getRegister(Value value) {
        return null;
    }

    public static RegDispatcher getInstance() {
        return regDispatcher;
    }

    public RegDispatcher() {
        this.currentFunction = null;
        this.currentOffset = 0;
        this.module = null;
        this.freeRegs = Register.tempRegs();
        this.usedRegs = new HashSet<>();
    }

    public void setModule(Module module) {
        this.module = module;
    }

    private void allocRegForValue(Value v) {
        if (!v.isDistributed()) {
            if (freeRegs.isEmpty()) {
                v.setOffset(currentOffset - 4);
                currentOffset = currentOffset - 4;
            } else {
                Register reg = freeRegs.iterator().next();
                freeRegs.remove(reg);
                usedRegs.add(reg);
                v.setUseReg(reg);
            }
        }
    }

    public void enterFunc(Function function) {
        OffsetMap.put(currentFunction, currentOffset);
        this.currentFunction = function;
        Val2Reg.put(function, new HashMap<>());
        Val2Offset.put(function, new HashMap<>());
        Reg2Val.put(function, new HashMap<>());
        OffsetMap.put(function, 0);
        flush();
    }

    private void flush() {
        currentOffset = 0;
        this.freeRegs = Register.tempRegs();
        this.usedRegs = new HashSet<>();
    }

    private void allocSpaceForElement(Value v) {
        //just alloc the mem , the pointer is usually defined by reg
        assert (v instanceof AllocaInst allocaInst);
        currentOffset = currentOffset - ((AllocaInst) v).getElementType().getLen();
        ((AllocaInst) v).setElementOffset(currentOffset);
    }

    public void distributeRegFor(Value v) {
        if (v instanceof AllocaInst allocaInst) {
            allocSpaceForElement(v);
        } else if (v instanceof BinaryInstr) {
            v.setUseReg(Register.K0);
        }
        allocRegForValue(v);
    }

    public int getCurrentOffset() {
        return currentOffset;
    }
}
