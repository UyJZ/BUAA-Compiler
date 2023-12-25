package MyPackage.IR;

import MyPackage.MIPS.MipsModule;
import MyPackage.MIPS.label;
import MyPackage.OutPut;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private ArrayList<User> instructions;
    private String label;

    public BasicBlock(Type type, int value, String label) {
        super(type, value);
        this.label = label;
        instructions = new ArrayList<>();
    }

    public ArrayList<User> getInstructions() {
        return instructions;
    }

    public void addInstruction(User user) {
        instructions.add(user);
    }

    public String getLabel() {
        return label + ":";
    }

    @Override
    public void print() {
        if (getType().equals(Type.Label)) {
            OutPut.printLlvm(label + ":\n");
        }
        for (int i = 0; i < instructions.size(); i++) {
            instructions.get(i).print();
        }
    }

    public void generateMips() {
        if (getType().equals(Type.Label)) {
            MipsModule.addText(new label(label));
        }
        for (int i = 0; i < instructions.size(); i++) {
            instructions.get(i).generateMips();
            if (MipsModule.stringBuilder != null) {
                if (MipsModule.isPut) {
                    MipsModule.isPut = false;
                }
                else {
                    MipsModule.addString();
                }
            }
        }
        if (MipsModule.stringBuilder != null) {
            MipsModule.isPut = false;
            MipsModule.addString();
        }
        MipsModule.regClear();
    }
}
