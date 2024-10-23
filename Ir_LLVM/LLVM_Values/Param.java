package Ir_LLVM.LLVM_Values;

import Config.Tasks;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Types.LLVMType;

public class Param extends LLVM_Value {
    public Param(LLVMType type) {
        super(type, Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
    }

    public Param(LLVMType type, String name) { // for const string
        super(type, name);
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }


}
