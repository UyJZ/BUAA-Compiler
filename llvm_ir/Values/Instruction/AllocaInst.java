package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.AluITAsm;
import BackEnd.MIPS.Assembly.AluRTAsm;
import BackEnd.MIPS.Assembly.BranchITAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import Config.tasks;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;
import Config.tasks.*;

import java.nio.channels.Pipe;

public class AllocaInst extends Instr {

    private int elementOffset;

    public AllocaInst(LLVMType type) {
        super(new PointerType(type), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
    }

    public void setElementOffset(int offset) {
        this.elementOffset = offset;
    }

    @Override
    public String toString() {
        return name + " = alloca " + ((PointerType) type).getElementType();
    }

    public LLVMType getElementType() {
        return ((PointerType) type).getElementType();
    }

    @Override
    public void genMIPS() {
        RegDispatcher.getInstance().distributeRegFor(this);
        if (this.useReg) {
            AluITAsm aluITAsm = new AluITAsm(AluITAsm.Op.addi, this.register, Register.SP, this.offset);
            MipsController.getInstance().addAsm(aluITAsm);
        } else {
            AluITAsm addi = new AluITAsm(AluITAsm.Op.addi, Register.K0, Register.SP, this.elementOffset);
            MipsController.getInstance().addAsm(addi);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
        }
    }
}
