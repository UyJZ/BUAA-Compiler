package IR_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.MipsBuilder;
import Config.Tasks;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.HashMap;

public class ZextInstr extends Instr {

    private final LLVMType type1;

    public ZextInstr(LLVMType type1, LLVMType type2, LLVM_Value operand) {
        super(type2, Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        this.addValue(operand);
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return name + " = " + "zext " + type1.toString() + " " + operands.get(0).getName() + " to " + type.toString();
    }

    @Override
    public void genMIPS() {
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(commentAsm);
        LLVM_Value operand = operands.get(0);
        //MIPS不存在bool
        this.useReg = operand.isUseReg();
        if (useReg) this.register = operand.getRegister();
        else this.offset = operand.getOffset();
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new ZextInstr(type1, type, operands.get(0).copy(map));
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
