package MidEnd;

import BackEnd.MIPS.Register;
import Ir_LLVM.LLVM_Module;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Function;
import Ir_LLVM.LLVM_Values.Instr.Instr;
import Ir_LLVM.LLVM_Values.Instr.MoveInstr;
import Ir_LLVM.LLVM_Values.Instr.PhiInstr;
import Ir_LLVM.LLVM_Values.Instr.ZextInstr;

import java.util.*;

public class RegAllocator {
    private final LLVM_Module LLVMModule;

    private final HashMap<LLVM_Value, Integer> useTimes;

    private HashMap<Function, HashSet<Register>> funcFreeRegs;

    private Function currentFunction;

    public RegAllocator(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
        this.useTimes = new HashMap<>();
        this.funcFreeRegs = new HashMap<>();
    }

    public void run() {
        buildUseTimes();
        for (Function function : LLVMModule.getFunctionList()) {
            currentFunction = function;
            LinkedHashSet<Register> regSetForAlloc = Register.tempRegs();
            HashSet<Register> freeRegs = new HashSet<>();
            for (Register value : Register.values()) {
                if (value.ordinal() >= Register.T0.ordinal() && value.ordinal() <= Register.T9.ordinal()) {
                    freeRegs.add(value);
                }
            }
            funcFreeRegs.put(function, freeRegs);
            allocRegForBlock(function.getBlockArrayList().get(0), regSetForAlloc);
        }
    }

    private void allocRegForBlock(BasicBlock block, LinkedHashSet<Register> freeRegs) {
        for (LLVM_Value LLVMValue : block.getInSet()) {
            if (LLVMValue.isUseReg()) {
                System.out.println(LLVMValue.getName() + " ==> " + LLVMValue.getRegister() + " (in)" + " " + LLVMValue.getName());
            }
        }
        for (Register register : freeRegs) {
            System.out.println("free: " + register);
        }
        HashMap<Register, LLVM_Value> reg2val = new HashMap<>();
        for (LLVM_Value LLVMValue : block.getInSet()) {
            if (LLVMValue.isUseReg())
                reg2val.put(LLVMValue.getRegister(), LLVMValue);
        }
        funcFreeRegs.get(currentFunction).removeAll(reg2val.keySet());
        HashMap<LLVM_Value, Instr> lastUse = new HashMap<>();
        for (Instr instr : block.getInstrs()) {
            if (instr instanceof MoveInstr moveInstr) {
                lastUse.put(moveInstr.getSrc(), instr);
            } else {
                for (LLVM_Value LLVMValue : instr.getOperands()) {
                    if (LLVMValue.isDistributable()) {
                        if (lastUse.containsKey(LLVMValue)) {
                            lastUse.replace(LLVMValue, instr);
                        } else {
                            lastUse.put(LLVMValue, instr);
                        }
                    }
                }
            }
        }
        for (Instr instr : block.getInstrs()) {
            if (instr instanceof MoveInstr moveInstr) {
                //TODO
                boolean isfree = false;
                if (moveInstr.getSrc().isDistributedToReg() && lastUse.get(moveInstr.getSrc()) == instr && !block.getOutSet().contains(moveInstr.getSrc()) && !(moveInstr.getSrc() instanceof PhiInstr)) {
                    //释放寄存器
                    System.out.println("lastuse free " + moveInstr.getSrc().getName() + " ==> " + moveInstr.getSrc().getRegister());
                    freeRegs.add(moveInstr.getSrc().getRegister());
                    reg2val.remove(moveInstr.getSrc().getRegister());
                    isfree = true;
                }
                //再分配寄存器
                if (!moveInstr.getDst().isDistributed()) {
                    if (isfree && moveInstr.getSrc().getRegister()!= Register.ZERO) {
                        moveInstr.getDst().setUseReg(moveInstr.getSrc().getRegister());
                        freeRegs.remove(moveInstr.getSrc().getRegister());
                        reg2val.put(moveInstr.getSrc().getRegister(), moveInstr.getDst());
                    }
                    else {
                         boolean flag = distributeRegFor(moveInstr.getDst(), freeRegs, reg2val);
                         if (!flag) {
                             //TODO:SPILL
                             spill(moveInstr.getDst(), reg2val, block.getInSet());
                         }
                    }
                }
            } else {
                //先释放寄存器
                for (LLVM_Value LLVMValue : instr.getOperands()) {
                    if (LLVMValue.isDistributedToReg() && lastUse.get(LLVMValue) == instr && !block.getOutSet().contains(LLVMValue) && !(LLVMValue instanceof PhiInstr)) {
                        //释放寄存器
                        freeRegs.add(LLVMValue.getRegister());
                        reg2val.remove(LLVMValue.getRegister());
                        System.out.println("lastuse free " + LLVMValue.getName() + " ==> " + LLVMValue.getRegister());
                    }
                }
                //再分配寄存器
                if (instr.isDefinition() && !instr.isDistributedToReg() && !(instr instanceof ZextInstr)) {
                    boolean flag = distributeRegFor(instr, freeRegs, reg2val);
                    if (!flag) {
                        //TODO:SPILL
                        spill(instr, reg2val, block.getInSet());
                    }
                }
            }
        }

        for (BasicBlock block1 : block.getImmDominatee()) {
            LinkedHashSet<Register> set = Register.tempRegs();
            for (LLVM_Value LLVMValue : block1.getInSet()) {
                if (LLVMValue.isUseReg()) {
                    set.remove(LLVMValue.getRegister());
                }
            }
            for (Map.Entry<Register, LLVM_Value> e : reg2val.entrySet()) {
                if (e.getValue() instanceof PhiInstr ) {
                    set.remove(e.getKey());
                }
            }
            allocRegForBlock(block1, set);
        }
    }

    private void spill(LLVM_Value LLVMValue, HashMap<Register, LLVM_Value> reg2val, HashSet<LLVM_Value> inset) {
        int times = useTimes.get(LLVMValue);
        LLVM_Value minVal = null;
        Register register = null;
        int min = 999999;
        for (Map.Entry<Register, LLVM_Value> e : reg2val.entrySet()) {
            if (useTimes.get(e.getValue()) < min && !inset.contains(e.getValue()) && !(e.getValue() instanceof PhiInstr)) {
                min = useTimes.get(e.getValue());
                minVal = e.getValue();
                register = e.getKey();
            }
        }
        if (times > min && minVal != null) {
            minVal.removeDistribute();
            LLVMValue.setUseReg(register);
            reg2val.put(register, LLVMValue);
            System.out.println("spill free " + minVal.getName() + " ==> " + register);
            System.out.println(LLVMValue.getName() + " ==> " + register);
        }
    }

    private void buildUseTimes() {
        for (Function function : LLVMModule.getFunctionList()) {
            for (BasicBlock block : function.getBlockArrayList()) {
                for (Instr instr : block.getInstrs()) {
                    if (instr instanceof MoveInstr moveInstr) {
                        if (useTimes.containsKey(moveInstr.getSrc())) {
                            useTimes.put(moveInstr.getSrc(), useTimes.get(moveInstr.getSrc()) + 1);
                        } else {
                            useTimes.put(moveInstr.getSrc(), 1);
                        }
                        continue;
                    }
                    for (LLVM_Value LLVMValue : instr.getOperands()) {
                        if (useTimes.containsKey(LLVMValue)) {
                            useTimes.put(LLVMValue, useTimes.get(LLVMValue) + 1);
                        } else {
                            useTimes.put(LLVMValue, 1);
                        }
                    }
                }
            }
        }
    }

    private boolean distributeRegFor(LLVM_Value v, LinkedHashSet<Register> freeRegs, HashMap<Register, LLVM_Value> reg2val) {
        //TODO
        if (!v.isDistributed() && !freeRegs.isEmpty()) {
            Register reg = freeRegs.iterator().next();
            v.setUseReg(reg);
            freeRegs.remove(reg);
            reg2val.put(reg, v);
            funcFreeRegs.get(currentFunction).remove(reg);
            System.out.println(v.getName() + " ==> " + reg);
            return true;
        }
        return false;
    }
}
