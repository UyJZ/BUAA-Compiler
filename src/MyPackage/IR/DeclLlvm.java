package MyPackage.IR;

import MyPackage.MIPS.MipsDecl;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;

import java.util.ArrayList;

public class DeclLlvm extends Value {
    private String name;
    private boolean isGlobal;
    private ArrayList<Integer> val;
    private ArrayList<Integer> dim;
    public DeclLlvm(Type type, int value, String name, boolean isConst) {
        super(type, value);
        this.name = name;
        this.isGlobal = isConst;
        val = new ArrayList<>();
        dim = new ArrayList<>();
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("@%s = dso_local ", name));
        if (isGlobal) {
            OutPut.printLlvm("constant ");
        }
        else {
            OutPut.printLlvm("global ");
        }
        if (!val.isEmpty()) {
            if (dim.size() == 0) {
                OutPut.printLlvm(String.format("%s %d\n", super.printType(), val.get(0)));
            } else {
                printArray(0, 0);
                OutPut.printLlvm("\n");
            }
        }
        else {
            if (dim.size() == 0) {
                OutPut.printLlvm(String.format("%s zeroinitializer\n", printType()));
            }
            else {
                OutPut.printLlvm(String.format("%s zeroinitializer\n", printArrayType(0)));
            }
        }
    }

    public ArrayList<Integer> getVal() {
        return val;
    }

    public void addDim(int n) {
        dim.add(n);
    }

    public String printArrayType(int index) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = index; i < dim.size(); i++) {
            stringBuilder.append('[');
            stringBuilder.append(dim.get(i));
            stringBuilder.append(" x ");
        }
        stringBuilder.append("i32");
        for (int i = index; i < dim.size(); i++) {
            stringBuilder.append(']');
        }
        if (getType().equals(Type.Pointer)) {
            stringBuilder.append('*');
        }
        return stringBuilder.toString();
    }

    public int printArray(int level, int index) {
        OutPut.printLlvm(String.format("%s [", printArrayType(level)));
        int p = index;
        if (level == dim.size() - 1) {
            for (int i = 0; i < dim.get(level) - 1; i++) {
                OutPut.printLlvm(String.format("i32 %d, ", val.get(p)));
                p++;
            }
            OutPut.printLlvm(String.format("i32 %d", val.get(p)));
            p++;
        }
        else {
            for (int i = 0; i < dim.get(level) - 1; i++) {
                p = printArray(level + 1, p);
                OutPut.printLlvm(", ");
            }
            p = printArray(level + 1, p);
        }
        OutPut.printLlvm("]");
        return p;
    }

    @Override
    public String printType() {
        if (dim.size() == 0) {
            return super.printType();
        }
        else {
            return printArrayType(0);
        }
    }

    public ArrayList<Integer> getDim() {
        return dim;
    }

    public void generateMips() {
        if (val.isEmpty()) {
            int size = 1;
            for (int i = 0; i < dim.size(); i++) {
                size *= dim.get(i);
            }
            MipsModule.addDate(new MipsDecl(name, size*4, "space"));
        }
        else {
            MipsModule.addDate(new MipsDecl(name, val, "word"));
        }
    }
}
