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
    //this offset will be stored in the value of reg or sp + offset
    //you can take this out by lw targetReg 0(reg) or
    //lw tempReg offset($sp) + lw targetReg 0(tempReg)

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
        //很明确，需要做两件事情，给值申请内存，给指针申请寄存器
        //首先，给值申请内存
        RegDispatcher.getInstance().distributeMemForAlloc(this);
        //然后，给指针申请寄存器
        RegDispatcher.getInstance().distributeRegFor(this);
        //最后，给指针赋值
        if (this.useReg) {
            //指针直接获得了寄存器
            AluITAsm aluITAsm = new AluITAsm(AluITAsm.Op.addi, this.register, Register.SP, this.elementOffset);
            //这个是指针志向的值的偏移量
            MipsController.getInstance().addAsm(aluITAsm);
        } else {
            AluITAsm addi = new AluITAsm(AluITAsm.Op.addi, Register.K0, Register.SP, this.elementOffset);
            MipsController.getInstance().addAsm(addi);
            //先算出来值的内存地址
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
            //把值存到记录指向值的地址的那块地址上
        }
    }
}
