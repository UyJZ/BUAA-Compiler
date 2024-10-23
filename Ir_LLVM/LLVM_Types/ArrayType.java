package Ir_LLVM.LLVM_Types;

import java.util.List;

public class ArrayType extends LLVMType {

    private LLVMType eleType;

    private List<Integer> lens;

    private int dim;

    public ArrayType(List<Integer> lens, LLVMType eleType) {
        super();
        this.lens = lens;
        this.eleType = eleType;
        this.dim = lens.size();
        int l = 1;
        for (int i = 0; i < dim; i++) {
            l *= lens.get(i);
        }
        this.len = l * 4;
    }

    public LLVMType getElementType() {
        if (dim == 2) return new ArrayType(lens.subList(1, 2), eleType);
        else return eleType;
    }

    public int getDim() {
        return dim;
    }

    public LLVMType getElementType(int i, int j) {
        return eleType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(lens.get(0));
        stringBuilder.append(" x ");
        stringBuilder.append(getElementType().toString());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
