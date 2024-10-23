package MidEnd;

import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Module;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.Instr.AllocaInst;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class RegDispatcher {
    private static final RegDispatcher regDispatcher = new RegDispatcher();

    private Function currentFunction;

    private LinkedHashSet<Register> freeRegs;

    private LinkedHashSet<Register> freeArgRegs;

    private LinkedHashSet<Register> usedArgRegs;

    private LinkedHashSet<Register> usedRegs;

    private HashMap<Function, HashMap<LLVM_Value, Register>> Val2Reg;

    private HashMap<Function, HashMap<LLVM_Value, Integer>> Val2Offset;

    private HashMap<Function, HashMap<Register, LLVM_Value>> Reg2Val;

    private HashMap<Function, Integer> OffsetMap;

    private int currentOffset;

    private LLVM_Module LLVMModule;

    public boolean hasFreeReg() {
        return false;
    }

    public Register getRegister(LLVM_Value LLVMValue) {
        return null;
    }

    public static RegDispatcher getInstance() {
        return regDispatcher;
    }

    public RegDispatcher() {
        this.currentFunction = null;
        this.currentOffset = 0;
        this.LLVMModule = null;
        this.freeRegs = Register.tempRegs();
        this.usedRegs = new LinkedHashSet<>();
        this.freeArgRegs = Register.argsRegs();
        this.Val2Reg = new HashMap<>();
        this.Val2Offset = new HashMap<>();
        this.Reg2Val = new HashMap<>();
        this.OffsetMap = new HashMap<>();
    }

    public void setModule(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
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
        this.freeArgRegs = function.getFreeArgRegs();
        this.usedArgRegs = function.getUsedArgRegs();
    }

    public void distributeMemForAlloc(AllocaInst v) {
        currentOffset = currentOffset - ((AllocaInst) v).getElementType().getLen();
        ((AllocaInst) v).setElementOffset(currentOffset);
    }

    public void distributeSpaceForVal(LLVM_Value v) {
        currentOffset = currentOffset - v.getLen();
        v.setOffset(currentOffset);
    }

    public void distributeRegFor(LLVM_Value v) {
        //这个条件目前是不确定的，也可以说是待定的，因为之后还有图着色算法，目前还没有确定无法被着色的val是否是isdistributed()
        if (v.isDistributedToReg()) {
            freeRegs.remove(v.getRegister());
            usedRegs.add(v.getRegister());
        } else if (v.isDistributedToMem()) {
            if (v.getOffset() > 0) {
                v.setOffset(currentOffset - v.getLen());
                currentOffset = currentOffset - v.getLen();
            }
        }
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
        return res;
    }

    public LinkedHashSet<Register> RegToPushInStackForParam() {
        return usedArgRegs;
    }

    public LinkedHashSet<Register> usedRegister() {
        LinkedHashSet<Register> res = new LinkedHashSet<>();
        res.addAll(usedRegs);
        res.addAll(usedArgRegs);
        return res;
    }

    public LinkedHashSet<Register> systemReg() {
        LinkedHashSet<Register> res = new LinkedHashSet<>();
        res.add(Register.RA);
        res.add(Register.SP);
        return res;
    }

    public void allocSpaceForReg() {
        currentOffset = currentOffset - 4;
    }

    public int getUsedRegsSize() {
        return usedRegs.size() * 4 + 8;
    }
}
