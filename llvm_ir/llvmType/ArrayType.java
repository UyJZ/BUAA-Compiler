package llvm_ir.llvmType;

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
    }

    public LLVMType getEleType() {
        if (dim == 2) return new ArrayType(lens.subList(1, 2), eleType);
        else return eleType;
    }

    public int getDim() {
        return dim;
    }

    public LLVMType getEleType(int i, int j) {
        return eleType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(lens.get(0));
        stringBuilder.append(" x ");
        stringBuilder.append(getEleType().toString());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
