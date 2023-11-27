package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class PhiInstr extends Instr {

    private ArrayList<Value> options;

    private ArrayList<Value> labels;

    private AllocaInst father;

    public PhiInstr(LLVMType type, AllocaInst inst) {
        super(type, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        options = new ArrayList<>();
        labels = new ArrayList<>();
        father = inst;
    }

    public void addOption(BasicBlock label, Value v) {
        labels.add(label);
        options.add(v);
    }

    public Value getFather() {
        return father;
    }
}
