package MyPackage.MIPS;


import MyPackage.OutPut;

import java.util.ArrayList;

public class MipsDecl implements Mips{
    String name;
    ArrayList<Integer> values;
    int size;
    String string = null;

    String type;

    public MipsDecl(String name, ArrayList<Integer> values, String type) {
        this.name = name;
        this.values = values;
        this.type = type;
    }

    public MipsDecl(String name, int size, String type) {
        this.name = name;
        this.size = size;
        this.type = type;
    }

    public MipsDecl(String name, String string, String type) {
        this.name = name;
        this.string = string;
        this.type = type;
    }
    public void print() {
        if (type.equals("word")) {
            OutPut.printMips2(String.format("  %s: .word ", name));
            for (int i = 0; i < values.size(); i++) {
                OutPut.printMips2(String.format("%d,", values.get(i)));
            }
            OutPut.printMips2("\n");
        }
        else if (type.equals("space")) {
            OutPut.printMips(String.format("  %s: .space %d", name, size));
        }
        else {
            OutPut.printMips(String.format("  %s: .asciiz \"%s\"", name, string));
        }
    }
}
