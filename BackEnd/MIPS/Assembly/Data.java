package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.MipsBuilder;
import Ir_LLVM.InitializedValue;

import java.util.ArrayList;

public class Data extends Asm {

    private ArrayList<Integer> lens;

    private InitializedValue initializedValue;

    private String name;

    private String ConString;


    private boolean isasciiz;

    public Data(ArrayList<Integer> lens, InitializedValue initializedValue, String name) {
        this.lens = lens;
        this.initializedValue = initializedValue;
        this.name = "global_" + name.substring(1);
        isasciiz = false;
    }

    public Data(String st) {
        this.ConString = st;
        this.name = MipsBuilder.getInstance().genConStrName();
        isasciiz = true;
    }

    public String toString() {
        if (isasciiz) {
            return this.name + ":\t" + ".asciiz   " + "\"" + ConString + "\"";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name).append(":\t");
            if (initializedValue == null) {
                int space = 4;
                for (int i : lens) space = space * i;
                sb.append("\n\t.space ").append(space);
            } else {
                sb.append(initializedValue.genMIPSData(lens));
            }
            return sb.toString();
        }
    }

    public String getName() {
        return name;
    }
}
