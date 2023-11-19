package llvm_ir.Values.Instruction;

import llvm_ir.IRController;
import llvm_ir.User;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.terminatorInstr.BranchInstr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class Instr extends User {
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

    public ArrayList<Value> getOperands() {
        return operands;
    }
}
