package llvm_ir.Values.Instruction;

import Config.tasks;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;

public class GEPInstr extends Instr {

    private Value ptr;

    private ArrayList<Value> indexs;

    private LLVMType genOutType(PointerType type) {
        LLVMType type1 = type.getElementType();
        if (indexs.size() == 2) {
            assert type1 instanceof ArrayType;
            return new PointerType(((ArrayType) type1).getElementType());
        } else return type;
    }

    public GEPInstr(Value ptr, ConstInteger index0, Value index) {
        super(new LLVMType(), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        indexs = new ArrayList<>();
        indexs.add(index0);
        indexs.add(index);
        this.ptr = ptr;
        this.type = genOutType((PointerType) ptr.getType());
        this.operands.add(ptr);
        this.operands.add(index);
    }

    public GEPInstr(Value ptr, Value index) {
        super(new LLVMType(), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        indexs = new ArrayList<>();
        indexs.add(index);
        this.ptr = ptr;
        this.operands.add(ptr);
        this.type = genOutType((PointerType) ptr.getType());
        this.operands.add(index);
    }

    public void setLLVMtypeForFuncParam() {
        int len = type.getLen();
        this.type = new PointerType(type);
        ((PointerType) this.type).setLen(len);
    }

    @Override
    public String toString() {
        if (indexs.size() == 1)
            return name + " = getelementptr " + ((PointerType) ptr.getType()).getElementType() + ", " + ptr.getType() + " " + ptr.getName() + ", i32 " + indexs.get(0).getName();
        return name + " = getelementptr " + ((PointerType) ptr.getType()).getElementType() + ", " + ptr.getType() + " " + ptr.getName() + ", i32 0, i32 " + indexs.get(1).getName();
    }
}
