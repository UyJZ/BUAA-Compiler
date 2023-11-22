package FrontEnd.Symbol;

import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.List;

public class Initial {

    private int dim;

    private LLVMType elementType;

    private ArrayList<ArrayList<Integer>> values; // dim == 0 时, lens.get(0).get(0) is the value, dim == 1时, len.get(0) is the value

    public Initial(int dim, ArrayList<ArrayList<Integer>> values) {
        this.dim = dim;
        this.values = values;
        this.elementType = new Integer32Type();
    }

    public LLVMType getElementType() {
        return elementType;
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

    public String GlobalVarLLVMir(ArrayList<Integer> lens, LLVMType elementType) {
        StringBuilder sb = new StringBuilder();
        if (lens.size() == 0) {
            sb.append(elementType).append(" ").append(values.get(0).get(0));
        } else if (lens.size() == 1) {
            if (values.get(0).size() == 0) {
                sb.append("[").append(lens.get(0)).append(" x ").append(elementType).append("]").append(" zeroinitializer ");
            } else {
                sb.append("[").append(lens.get(0)).append(" x ").append(elementType).append("] [");
                for (int i = 0; i < values.get(0).size(); i++) {
                    sb.append(elementType).append(" ").append(values.get(0).get(i));
                    if (i != values.get(0).size() - 1) sb.append(" ,");
                }
                for (int i = values.get(0).size(); i < lens.get(0); i++) {
                    sb.append(" ,").append(elementType).append(" 0");
                }
                sb.append(" ]");
            }
        } else {
            sb.append("[").append(lens.get(0)).append(" x").append("[").append(lens.get(1)).append(" x ").append(elementType).append("] ]");
            ArrayList<OneDimArray> OneDimArrays = new ArrayList<>();
            for (int i = 0; i < lens.get(0); i++) {
                if (i >= values.size()) {
                    OneDimArrays.add(new OneDimArray(lens.get(1), new ArrayList<>(), elementType));
                } else {
                    OneDimArrays.add(new OneDimArray(lens.get(1), values.get(i), elementType));
                }
            }
            int allZeroPos = lens.get(0);
            for (int i = OneDimArrays.size() - 1; i >= 0; i--) {
                if (OneDimArrays.get(i).allZero) {
                    allZeroPos = i;
                } else break;
            }
            sb.append(" [");
            for (int i = 0; i < allZeroPos; i++) {
                sb.append(OneDimArrays.get(i).getValue());
                if (i != allZeroPos - 1) sb.append(", ");
            }
            for (int i = allZeroPos; i < lens.get(0); i++)
                sb.append(", ").append(" [").append(lens.get(1)).append(" x ").append(elementType).append("] ").append("zeroinitializer");
            sb.append("]");
        }
        return sb.toString();
    }

    public String genMIPSData(ArrayList<Integer> lens) {
        StringBuilder sb = new StringBuilder();
        if (dim == 0) {
            sb.append(".word \t").append(values.get(0).get(0));
        } else if (dim == 1) {
            sb.append("\t.word \t");
            for (int i : values.get(0)) sb.append(i).append(", ");
            if (lens.get(0) > values.get(0).size()) {
                sb.append("\n\t.space \t").append(4 * (lens.get(0) - values.get(0).size()));
            }
        } else {
            assert dim == 2;
            ArrayList<OneDimArray> OneDimArrays = new ArrayList<>();
            for (int i = 0; i < lens.get(0); i++) {
                if (i >= values.size()) {
                    OneDimArrays.add(new OneDimArray(lens.get(1), new ArrayList<>(), elementType));
                } else {
                    OneDimArrays.add(new OneDimArray(lens.get(1), values.get(i), elementType));
                }
            }
            int allZeroPos = lens.get(0);
            for (int i = OneDimArrays.size() - 1; i >= 0; i--) {
                if (OneDimArrays.get(i).allZero) {
                    allZeroPos = i;
                } else break;
            }
            for (int i = 0; i < allZeroPos; i++) {
                sb.append(OneDimArrays.get(i).getMIPSVal()).append("\n");
            }
            if (allZeroPos < lens.get(0)) {
                sb.append("\t.space ").append(4 * (lens.get(1) * (lens.get(0) - allZeroPos)));
            }
        }
        return sb.toString();
    }

    private class OneDimArray {

        public String type;

        public ArrayList<Integer> values;

        public LLVMType elementType;

        public int len;

        public boolean allZero;

        public int ArrayAllZeroPos;

        public OneDimArray(int len, ArrayList<Integer> values, LLVMType type) {
            this.len = len;
            this.elementType = type;
            this.values = values;
            this.allZero = true;
            for (int i = 0; i < values.toArray().length; i++) {
                if (values.get(i) != 0) {
                    allZero = false;
                    break;
                }
            }
            int allzeroPos = values.toArray().length;
            for (int i = values.toArray().length - 1; i >= 0; i--) {
                if (values.get(i) == 0) {
                    allzeroPos = i;
                } else break;
            }
            ArrayAllZeroPos = allzeroPos;
            StringBuilder sb = new StringBuilder();
            sb.append(" [");
            sb.append(len).append(" * ").append(elementType);
            sb.append("] ");
            this.type = sb.toString();
        }

        public String getValue() {
            StringBuilder sb = new StringBuilder();
            if (allZero) {
                sb.append("[").append(" ").append(len).append(" x ").append(elementType).append("] ").append("zeroinitializer");
            } else {
                sb.append(" [").append(len).append(" x ").append(elementType).append(" ] [");
                for (int i = 0; i < values.toArray().length; i++) {
                    sb.append(elementType).append(" ").append(values.get(i));
                    if (i != values.size() - 1) sb.append(" ,");
                }
                for (int i = values.size(); i < len; i++) {
                    sb.append(" ,").append(elementType).append(" 0");
                }
                sb.append("]");
            }
            return sb.toString();
        }

        public String getMIPSVal() {
            StringBuilder sb = new StringBuilder();
            if (allZero) {
                sb.append("\t.space \t").append(len * 4);
            } else {
                sb.append("\t.word \t");
                for (int i = 0; i < values.size(); i++) {
                    sb.append(values.get(i));
                    if (i != values.size() - 1) sb.append(", ");
                }
                if (len > values.size())
                    sb.append("\n\t.space \t").append(4 * (len - values.size()));
            }
            return sb.toString();
        }
    }

}
