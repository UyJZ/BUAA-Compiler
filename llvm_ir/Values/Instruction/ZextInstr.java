package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.MipsController;
import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;

public class ZextInstr extends Instr {

    private final LLVMType type1;

    private final Value operand;

    public ZextInstr(LLVMType type1, LLVMType type2, Value operand) {
        super(type2, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.operand = operand;
        this.addValue(operand);
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return name + " = " + "zext " + type1.toString() + " " + operand.getName() + " to " + type.toString();
    }

    @Override
    public void genMIPS() {
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(commentAsm);
        //MIPS不存在bool
        this.useReg = operand.isUseReg();
        if (useReg) this.register = operand.getRegister();
        else this.offset = operand.getOffset();
    }
}
