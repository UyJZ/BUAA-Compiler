package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.MipsController;
import FrontEnd.Symbol.Initial;

import java.util.ArrayList;

public class Data extends Asm {

    private ArrayList<Integer> lens;

    private Initial initial;

    private String name;

    private String ConString;


    private boolean isasciiz;

    public Data(ArrayList<Integer> lens, Initial initial, String name) {
        this.lens = lens;
        this.initial = initial;
        this.name = name;
        isasciiz = false;
    }

    public Data(String st) {
        this.ConString = st;
        this.name = MipsController.getInstance().genConStrName();
        isasciiz = true;
    }

    public String toString() {
        if (isasciiz) {
            return this.name + "\t" + ".asciiz" + "\"" + ConString + "\"";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name).append("\t");
            if (initial == null) {
                int space = 4;
                for (int i : lens) space = space * i;
                sb.append("\n\t.space").append(space);
            } else {
                sb.append(initial.genMIPSData(lens));
            }
            return sb.toString();
        }
    }
}
