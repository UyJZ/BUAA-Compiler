package IR_LLVM.LLVM_Values.Instr;

import IR_LLVM.LLVM_User;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.ReturnInstr;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.HashMap;
import java.util.HashSet;

public class Instr extends LLVM_User {

    protected BasicBlock fatherBlock;

    public Instr(LLVMType type, String name) {
        super(type, name);
    }

    public void setName() {
        if (this instanceof StoreInstr || this instanceof ReturnInstr || this instanceof BranchInstr) return;
        else this.name = LLVM_Builder.getInstance().genVirtualRegNum();
    }

    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
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
        for (LLVM_Value LLVMValue : usedByList) {
            if (LLVMValue instanceof Instr instr && !instr.canBeDeleted(deadInstrSet, records)) return false;
        }
        deadInstrSet.add(this);
        return true;
    }
}
