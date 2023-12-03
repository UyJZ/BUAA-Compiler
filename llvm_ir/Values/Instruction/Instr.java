package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Register;
import llvm_ir.IRController;
import llvm_ir.User;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Instr extends User {

    protected BasicBlock fatherBlock;

    public Instr(LLVMType type, String name) {
        super(type, name);
    }

    public void setName() {
        if (this instanceof StoreInstr || this instanceof ReturnInstr || this instanceof BranchInstr) return;
        else this.name = IRController.getInstance().genVirtualRegNum();
    }

    public boolean hasOutput() {
        return !(this instanceof StoreInstr || this instanceof BranchInstr || this instanceof ReturnInstr);
    }

    public Instr copy(HashMap<Value, Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return null;
    }

    public void setFatherBlock(BasicBlock fatherBlock) {
        this.fatherBlock = fatherBlock;
    }

    public BasicBlock getFatherBlock() {
        return fatherBlock;
    }

    public String GVNHash() {
        return null;
    }

    public boolean isPinnedInst() {
        return false;
    }

    public boolean isDefinition() {
        return false;
    }

    public boolean canBeDeleted(HashSet<Instr> deadInstrSet, HashSet<Instr> records) {
        if (this instanceof BranchInstr || this instanceof ReturnInstr || this instanceof CallInstr || this instanceof StoreInstr)
            return false;
        if (deadInstrSet.contains(this)) return true;
        if (records.contains(this)) return false;
        records.add(this);
        for (Value value : usedByList) {
            if (value instanceof Instr instr && !instr.canBeDeleted(deadInstrSet, records)) return false;
        }
        deadInstrSet.add(this);
        return true;
    }
}
