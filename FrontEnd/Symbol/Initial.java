package FrontEnd.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Initial {

    private int dim;

    private ArrayList<ArrayList<Integer>> values; // dim == 0 时, lens.get(0).get(0) is the value, dim == 1时, len.get(0) is the value

    public Initial(int dim, ArrayList<ArrayList<Integer>> values) {
        this.dim = dim;
        this.values = values;
    }

    public ArrayList<ArrayList<Integer>> getValues() {
        return values;
    }

    public int getDim() {
        return dim;
    }

    public void addInitial(Initial initial) {
        if (getDim() == 1) {
            values.get(0).add(initial.getValues().get(0).get(0));
        } else {
            values.add(initial.getValues().get(0));
        }
    }

    @Override
    public String toString() {
        if (getDim() == 0) {
            return values.get(0).get(0).toString();
        } else if (getDim() == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (int i = 0; i < values.get(0).size(); i++) {
                stringBuilder.append(values.get(0).get(i));
                if (i != values.get(0).size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("]");
            return stringBuilder.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (int i = 0; i < values.size(); i++) {
                stringBuilder.append("[");
                for (int j = 0; j < values.get(i).size(); j++) {
                    stringBuilder.append(values.get(i).get(j));
                    if (j != values.get(i).size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                stringBuilder.append("]");
                if (i != values.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }
    public int getVal(List<Integer> pos) {
        try {
            if (dim == 0) return values.get(0).get(0);
            else if (dim == 1) return values.get(0).get(pos.get(0));
            else return values.get(pos.get(0)).get(pos.get(1));
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }
}
