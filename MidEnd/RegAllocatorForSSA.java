package MidEnd;

import BackEnd.MIPS.Register;
import llvm_ir.Module;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.MoveInstr;
import llvm_ir.Values.Instruction.PhiInstr;
import llvm_ir.Values.Instruction.ZextInstr;

import java.util.*;

public class RegAllocatorForSSA {
    private final Module module;

    private final HashMap<Value, Integer> useTimes;

    private HashSet<Register> allPhiRegs;

    private HashMap<Function, HashSet<Register>> funcFreeRegs;

    private Function currentFunction;

    public RegAllocatorForSSA(Module module) {
        this.module = module;
        this.useTimes = new HashMap<>();
        this.funcFreeRegs = new HashMap<>();
        this.allPhiRegs = new HashSet<>();
    }

    public void run() {
        buildUseTimes();
        for (Function function : module.getFunctionList()) {
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
        for (Value value : block.getInSet()) {
            if (value.isUseReg()) {
                System.out.println(value.hash + " ==> " + value.getRegister() + " (in)" + " " + value.hash);
            }
        }
        for (Register register : freeRegs) {
            System.out.println("free: " + register);
        }
        HashMap<Register, Value> reg2val = new HashMap<>();
        for (Value value : block.getInSet()) {
            if (value.isUseReg())
                reg2val.put(value.getRegister(), value);
        }
        funcFreeRegs.get(currentFunction).removeAll(reg2val.keySet());
        HashMap<Value, Instr> lastUse = new HashMap<>();
        for (Instr instr : block.getInstrs()) {
            if (instr instanceof MoveInstr moveInstr) {
                lastUse.put(moveInstr.getSrc(), instr);
            } else {
                for (Value value : instr.getOperands()) {
                    if (value.isDistributable()) {
                        if (lastUse.containsKey(value)) {
                            lastUse.replace(value, instr);
                        } else {
                            lastUse.put(value, instr);
                        }
                    }
                }
            }
        }
        for (Instr instr : block.getInstrs()) {
            //先释放寄存器
            if (!(instr instanceof PhiInstr)) {
                for (Value value : instr.getOperands()) {
                    if (value.isDistributedToReg() && lastUse.get(value) == instr && !block.getOutSet().contains(value) && !(value instanceof PhiInstr)) {
                        //释放寄存器
                        freeRegs.add(value.getRegister());
                        reg2val.remove(value.getRegister());
                        System.out.println("lastuse free " + value.hash + " ==> " + value.getRegister());
                    }
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

        for (BasicBlock block1 : block.getImmDominatee()) {
            LinkedHashSet<Register> set = Register.tempRegs();
            for (Value value : block1.getInSet()) {
                if (value.isUseReg()) {
                    set.remove(value.getRegister());
                }
            }
            for (Map.Entry<Register, Value> e : reg2val.entrySet()) {
                if (e.getValue() instanceof PhiInstr) {
                    set.remove(e.getKey());
                }
            }
            allocRegForBlock(block1, set);
        }
    }

    private void spill(Value value, HashMap<Register, Value> reg2val, HashSet<Value> inset) {
        int times = useTimes.get(value);
        Value minVal = null;
        Register register = null;
        int min = 999999;
        for (Map.Entry<Register, Value> e : reg2val.entrySet()) {
            if (useTimes.get(e.getValue()) < min && !inset.contains(e.getValue()) && !(e.getValue() instanceof PhiInstr)) {
                min = useTimes.get(e.getValue());
                minVal = e.getValue();
                register = e.getKey();
            }
        }
        if (times > min && minVal != null) {
            minVal.removeDistribute();
            value.setUseReg(register);
            reg2val.put(register, value);
            if (value instanceof PhiInstr) {
                allPhiRegs.add(register);
            }
            System.out.println("spill free " + minVal.hash + " ==> " + register);
            System.out.println(value.hash + " ==> " + register);
        }
    }

    private void buildUseTimes() {
        for (Function function : module.getFunctionList()) {
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
                    for (Value value : instr.getOperands()) {
                        if (useTimes.containsKey(value)) {
                            useTimes.put(value, useTimes.get(value) + 1);
                        } else {
                            useTimes.put(value, 1);
                        }
                    }
                }
            }
        }
    }

    private boolean distributeRegFor(Value v, LinkedHashSet<Register> freeRegs, HashMap<Register, Value> reg2val) {
        //TODO
        if (!v.isDistributed() && !freeRegs.isEmpty()) {
            Register reg = freeRegs.iterator().next();
            v.setUseReg(reg);
            freeRegs.remove(reg);
            reg2val.put(reg, v);
            funcFreeRegs.get(currentFunction).remove(reg);
            System.out.println(v.hash + " ==> " + reg);
            if (v instanceof PhiInstr) {
                allPhiRegs.add(reg);
            }
            return true;
        }
        return false;
    }
}
