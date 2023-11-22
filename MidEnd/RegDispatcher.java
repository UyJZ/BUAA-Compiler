package MidEnd;

import BackEnd.MIPS.Register;
import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.Function;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.BinaryInstr;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.ArrayType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class RegDispatcher {
    private static final RegDispatcher regDispatcher = new RegDispatcher();

    private Function currentFunction;

    private LinkedHashSet<Register> freeRegs;

    private LinkedHashSet<Register> freeArgRegs;

    private LinkedHashSet<Register> usedRegs;

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
        this.usedRegs = new LinkedHashSet<>();
        this.freeRegs = Register.argsRegs();
        this.Val2Reg = new HashMap<>();
        this.Val2Offset = new HashMap<>();
        this.Reg2Val = new HashMap<>();
        this.OffsetMap = new HashMap<>();
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void enterFunc(Function function) {
        this.currentFunction = function;
        Val2Reg.put(function, function.getVal2Reg());
        Val2Offset.put(function, function.getVal2Offset());
        Reg2Val.put(function, function.getReg2Val());
        OffsetMap.put(function, function.getOffset());
        flush(function);
    }

    public void leaveFunc() {
        OffsetMap.put(currentFunction, currentOffset);
        this.currentOffset = 0;
    }

    private void flush(Function function) {
        currentOffset = function.getOffset();
        this.freeRegs = function.getFreeRegs();
        this.usedRegs = function.getUsedRegs();
    }

    public void distributeMemForAlloc(AllocaInst v) {
        currentOffset = currentOffset - ((AllocaInst) v).getElementType().getLen();
        ((AllocaInst) v).setElementOffset(currentOffset);
    }

    public void distributeSpaceForVal(Value v) {
        currentOffset = currentOffset - v.getLen();
        v.setOffset(currentOffset);
    }

    public void distributeRegFor(Value v) {
        //这个条件目前是不确定的，也可以说是待定的，因为之后还有图着色算法，目前还没有确定无法被着色的val是否是isdistributed()
        if (v.isDistributed() && v.isUseReg()) return;
        if (!v.isDistributed()) {
            if (freeRegs.isEmpty()) {
                v.setOffset(currentOffset - v.getLen());
                currentOffset = currentOffset - v.getLen();
            } else {
                Register reg = freeRegs.iterator().next();
                freeRegs.remove(reg);
                usedRegs.add(reg);
                v.setUseReg(reg);
            }
        }
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public LinkedHashSet<Register> RegToPushInStack() {
        LinkedHashSet<Register> res = new LinkedHashSet<>();
        res.add(Register.SP);
        res.add(Register.RA);
        res.addAll(usedRegs);
        return res;
    }

    public void allocSpaceForReg() {
        currentOffset = currentOffset - 4;
    }

    public int getUsedRegsSize() {
        return usedRegs.size() * 4 + 8;
    }
}
