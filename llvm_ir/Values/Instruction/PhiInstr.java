package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
        } else {
            if (label == null) {
                System.out.println("null label");
            }
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
        sb.append(name).append(" = phi ").append(type).append(" ");
        for (int i = 0; i < operands.size(); i++) {
            sb.append("[ ").append(operands.get(i).getName()).append(", ").append(labels.get(i).getName()).append(" ]");
            if (i != operands.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("  ;").append(father.getAllocaNum());
        return sb.toString();
    }

    @Override
    public Instr copy(HashMap<Value, Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        PhiInstr phiInstr = new PhiInstr(type, new AllocaInst(type));
        for (int i = 0; i < operands.size(); i++) {
            phiInstr.addOption((BasicBlock) labels.get(i).copy(map), operands.get(i).copy(map));
        }
        return phiInstr;
    }

    public ArrayList<BasicBlock> getLabels() {
        return labels;
    }
}
