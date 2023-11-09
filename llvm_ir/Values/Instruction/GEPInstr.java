package llvm_ir.Values.Instruction;

import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;

public class GEPInstr extends Instr {

    private String ptr;

    private ArrayList<Value> indexs;

    private VarSymbol symbol;

    private LLVMType fatherType;

    public GEPInstr(ArrayType type, String ptr, Value index) {
        super(type.getEleType(), IRController.getInstance().genVirtualRegNum());
        indexs = new ArrayList<>();
        indexs.add(index);
        this.ptr = ptr;
        this.fatherType = type;
    }

    public GEPInstr(PointerType type, String ptr, Value index) {
        super(type.getElementType(), IRController.getInstance().genVirtualRegNum());
        indexs = new ArrayList<>();
        indexs.add(index);
        this.ptr = ptr;
        this.fatherType = type;
    }

    public void setLLVMtypeForFuncParam() {
         this.type = new PointerType(type);
    }

    @Override
    public String toString() {
        if (fatherType instanceof PointerType)
            return name + " = getelementptr " + type.toString() + ", " + fatherType.toString() + " " + ptr + ", i32 " + indexs.get(0).getName();
        return name + " = getelementptr " + fatherType.toString() + ", " + fatherType.toString() + "* " + ptr + ", i32 0, i32 " + indexs.get(0).getName();
    }
}
