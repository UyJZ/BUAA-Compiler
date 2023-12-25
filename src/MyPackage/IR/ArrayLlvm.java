package MyPackage.IR;


import java.util.ArrayList;

public class ArrayLlvm extends Value{
    ArrayList<Integer> dim;

    public ArrayLlvm(Type type, int value) {
        super(type, value);
        dim = new ArrayList<>();
    }

    public void addDim(int n) {
        dim.add(n);
    }

    public ArrayList<Integer> getDim() {
        return dim;
    }

    @Override
    public String printType() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dim.size(); i++) {
            stringBuilder.append('[');
            stringBuilder.append(dim.get(i));
            stringBuilder.append(" x ");
        }
        stringBuilder.append("i32");
        for (int i = 0; i < dim.size(); i++) {
            stringBuilder.append(']');
        }
        if (getType().equals(Type.Pointer)) {
            stringBuilder.append('*');
        }
        return stringBuilder.toString();
    }

    public void setDim(ArrayList<Integer> dim) {
        this.dim = dim;
    }

    public int getSize() {
        int size = 4;
        for (int i = 0; i < dim.size(); i++) {
            size *= dim.get(i);
        }
        return size;
    }

}
