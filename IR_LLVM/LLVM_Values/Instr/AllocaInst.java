package IR_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Config.Tasks;
import MidEnd.RegDispatcher;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Types.LLVMType;
import IR_LLVM.LLVM_Types.PointerType;

import java.util.HashMap;

public class AllocaInst extends Instr {

    private int elementOffset;

    public static int allocaNum = 0;

    private int allocOrder = 0;
    //this offset will be stored in the value of reg or sp + offset
    //you can take this out by lw targetReg 0(reg) or
    //lw tempReg offset($sp) + lw targetReg 0(tempReg)

    public AllocaInst(LLVMType type) {
        super(new PointerType(type), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        allocOrder = allocaNum++;
    }

    public int getAllocaNum() {
        return allocOrder;
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
        CommentAsm asm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(asm);
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
            MipsBuilder.getInstance().addAsm(aluITAsm);
        } else {
            AluITAsm addi = new AluITAsm(AluITAsm.Op.addi, Register.K0, Register.SP, this.elementOffset);
            MipsBuilder.getInstance().addAsm(addi);
            //先算出来值的内存地址
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
            MipsBuilder.getInstance().addAsm(sw);
            //把值存到记录指向值的地址的那块地址上
        }
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new AllocaInst(((PointerType) type).getElementType());
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
