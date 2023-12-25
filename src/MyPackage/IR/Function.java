package MyPackage.IR;


import MyPackage.MIPS.Mips;
import MyPackage.MIPS.MipsModule;
import MyPackage.MIPS.label;
import MyPackage.OutPut;

import java.util.ArrayList;

public class Function extends Value {
    private String name;
    private ArrayList<Value> params;
    private ArrayList<String> paramsName;
    private ArrayList<BasicBlock> basicBlocks;

    public Function(Type type, int value, String name) {
        super(type, value);
        this.name = name;
        params = new ArrayList<>();
        basicBlocks = new ArrayList<>();
        paramsName = new ArrayList<>();
    }

    public ArrayList<Value> getParams() {
        return params;
    }

    public ArrayList<String> getParamsName() {
        return paramsName;
    }

    public void addParams(Value value, String name) {
        params.add(value);
        paramsName.add(name);
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public void newBlock(String label) {
        basicBlocks.add(new BasicBlock(Type.Label, 0, label));
    }

    public void newBlock() {
        basicBlocks.add(new BasicBlock(Type.BasicBlock, 0, ""));
    }

    public BasicBlock getCurrentBlock() {
        return basicBlocks.get(basicBlocks.size() - 1);
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("define dso_local %s @%s(", super.printType(), name));
        for (int i = 0; i < params.size(); i++) {
            OutPut.printLlvm(String.format("%s %s", params.get(i).printType(), params.get(i).printValue()));
            if (i != params.size() - 1) {
                OutPut.printLlvm(", ");
            }
        }
        OutPut.printLlvm(") {\n");
        for (int i = 0; i < basicBlocks.size(); i++) {
            basicBlocks.get(i).print();
        }
        OutPut.printLlvm("}\n");
    }

    public void generateMips() {
        MipsModule.newStack();
        MipsModule.tableClear();
        if (name.equals("main")) {
            MipsModule.addText(new label("main"));
            MipsModule.isMain = true;
        }
        else {
            MipsModule.isMain = false;
            MipsModule.addText(new label(name));
            MipsModule.addStack(4);
        }
        for (int i = 0; i < params.size(); i++) {
            if (i < 4) {
                MipsModule.addParam(params.get(i).getValue(), String.format("$a%d", i));
            }
            else {
                MipsModule.addAddress(params.get(i).getValue(), MipsModule.getStack());
                MipsModule.addStack(4);
            }
        }
        for (int i = 0; i < basicBlocks.size(); i++) {
            basicBlocks.get(i).generateMips();
        }
    }
}
