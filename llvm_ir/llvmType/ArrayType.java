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
}
