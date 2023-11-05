package llvm_ir;

import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class Initial {
    private int dim;
    private LLVMType elementType;

    private ArrayList<Integer> values;

    public Initial(int dim, LLVMType elementType, ArrayList<Integer> values) {
        this.dim = dim;
        this.elementType = elementType;
        this.values = values;
    }
}
