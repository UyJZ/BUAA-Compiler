package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.Collection;

public class PhiInstr extends Instr {

    private ArrayList<BasicBlock> labels;

    private AllocaInst father;

    public PhiInstr(LLVMType type, AllocaInst inst) {
        super(type, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        labels = new ArrayList<>();
        father = inst;
    }

    public void addOption(BasicBlock label, Value v) {
        if (labels.contains(label)) {
            operands.set(labels.indexOf(label), v);
            operands.set(labels.indexOf(label), v);
            setValue(labels.indexOf(label), v);
        }
        else {
            labels.add(label);
            addValue(v);
        }
    }

    public Value getFather() {
        return father;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (father.getAllocaNum() == 0) {
            System.out.println("DEBUG");
        }
        sb.append(name).append(" = phi ").append(type).append(" ");
        for (int i = 0; i < operands.size(); i++) {
            if (labels.get(i).getName().equals("0")) {
                System.out.println(labels.get(i).getName());
            }
            sb.append("[ ").append(operands.get(i).getName()).append(", ").append(labels.get(i).getName()).append(" ]");
            if (i != operands.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("  ;").append(father.getAllocaNum());
        return sb.toString();
    }
}
