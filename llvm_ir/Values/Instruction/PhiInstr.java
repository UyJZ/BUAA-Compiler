package llvm_ir.Values.Instruction;

import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.BasicBlock;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class PhiInstr extends Instr {

    private AllocaInst father;

    public PhiInstr(LLVMType type, AllocaInst inst) {
        super(type, tasks.isSetNameAfterGen ? "" : IRController.getInstance().genVirtualRegNum());
        father = inst;
    }

    public void addOption(BasicBlock label, Value v) {
        ArrayList<BasicBlock> labels = new ArrayList<>();
        for (int i = 0; 2 * i < operands.size(); i++) {
            labels.add((BasicBlock) operands.get(2 * i));
        }
        if (operands.contains(label)) {
            operands.set(2 * labels.indexOf(label) + 1, v);
            operands.set(2 * labels.indexOf(label) + 1, v);
            setValue(2 * labels.indexOf(label) + 1, v);
        } else {
            if (label == null) {
                System.out.println("null label");
            }
            addValue(label);
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
        for (int i = 0; 2 * i < operands.size(); i++) {
            sb.append("[ ").append(operands.get(2 * i + 1).getName()).append(", ").append(operands.get(2 * i).getName()).append(" ]");
            if (2 * i + 2 < operands.size()) {
                sb.append(", ");
            }
        }
        sb.append("  ;").append(father.getAllocaNum());
        return sb.toString();
    }

    @Override
    public Instr copy(HashMap<Value, Value> map) {
        ArrayList<BasicBlock> labels = new ArrayList<>();
        for (int i = 0; 2 * i < operands.size(); i++) {
            labels.add((BasicBlock) operands.get(2 * i));
        }
        if (map.containsKey(this)) return (Instr) map.get(this);
        PhiInstr phiInstr = new PhiInstr(type, new AllocaInst(type));
        for (int i = 0; i < operands.size(); i++) {
            phiInstr.addOption((BasicBlock) labels.get(i).copy(map), operands.get(2 * i + 1).copy(map));
        }
        return phiInstr;
    }

    public ArrayList<BasicBlock> getLabels() {
        ArrayList<BasicBlock> labels = new ArrayList<>();
        for (int i = 0; 2 * i < operands.size(); i++) {
            labels.add((BasicBlock) operands.get(2 * i));
        }
        return labels;
    }

    public ArrayList<Value> getValues() {
        ArrayList<Value> values = new ArrayList<>();
        for (int i = 0; 2 * i < operands.size(); i++) {
            values.add(operands.get(2 * i + 1));
        }
        return values;
    }

    public void replaceBlock(BasicBlock block0, BasicBlock block) {
        ArrayList<BasicBlock> labels = new ArrayList<>();
        for (int i = 0; 2 * i < operands.size(); i++) {
            labels.add((BasicBlock) operands.get(2 * i));
        }
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) == block0) {
                operands.set(2 * i, block);
                block.addUsedBy(this);
            }
        }
    }

    @Override
    public boolean isPinnedInst() {
        return true;
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
