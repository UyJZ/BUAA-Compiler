package MyPackage.IR;

import java.util.ArrayList;

public class Value {
    private Type type;
    private int value;
    private ArrayList<Use> uses;

    public Value (Type type, int value) {
        this.type = type;
        this.value = value;
        uses = new ArrayList<>();
    }

    public void addUse(Use use) {
        uses.add(use);
    }

    public ArrayList<Use> getUses() {
        return uses;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String printValue() {
        if (type.equals(Type.MyConst)) {
            return String.format("%d", value);
        }
        else if (type.equals(Type.Void)) {
            return "";
        }
        else {
            return String.format("%%%d", value);
        }
    }

    public String printType() {
        if (type.equals(Type.Reg) || type.equals(Type.MyConst)) {
            return "i32";
        }
        else if (type.equals(Type.Pointer)) {
            return "i32*";
        }
        else if (type.equals(Type.Void)) {
            return "void";
        }
        else if (type.equals(Type.Boolean)) {
            return "i1";
        }
        else {
            return "i32";
        }
    }

    public void print() {

    }

    public void generateMips() {
    }
}
