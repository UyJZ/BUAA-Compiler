package Ir_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Config.Tasks;
import Ir_LLVM.LLVM_Value;
import MidEnd.RegDispatcher;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Types.ArrayType;
import Ir_LLVM.LLVM_Types.LLVMType;
import Ir_LLVM.LLVM_Types.PointerType;

import java.util.ArrayList;
import java.util.HashMap;

public class GEPInstr extends Instr {

    private LLVMType genOutType(PointerType type) {
        ArrayList<LLVM_Value> indexs = new ArrayList<>(this.operands.subList(1, this.operands.size()));
        LLVMType type1 = type.getElementType();
        if (indexs.size() == 2) {
            assert type1 instanceof ArrayType;
            return new PointerType(((ArrayType) type1).getElementType());
        } else return type;
    }

    public GEPInstr(LLVM_Value ptr, ConstInteger index0, LLVM_Value index) {
        super(new LLVMType(), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        this.addValue(ptr);
        this.addValue(index0);
        this.addValue(index);
        this.type = genOutType((PointerType) ptr.getType());
    }

    public GEPInstr(LLVM_Value ptr, LLVM_Value index) {
        super(new LLVMType(), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        this.addValue(ptr);
        this.addValue(index);
        this.type = genOutType((PointerType) ptr.getType());
    }

    @Override
    public String toString() {
        ArrayList<LLVM_Value> indexs = new ArrayList<>(this.operands.subList(1, this.operands.size()));
        LLVM_Value ptr = operands.get(0);
        if (indexs.size() == 1)
            return name + " = getelementptr " + ((PointerType) ptr.getType()).getElementType() + ", " + ptr.getType() + " " + ptr.getName() + ", i32 " + operands.get(1).getName();
        return name + " = getelementptr " + ((PointerType) ptr.getType()).getElementType() + ", " + ptr.getType() + " " + ptr.getName() + ", i32 0, i32 " + operands.get(2).getName();
    }


    /*
    理一下GEP的MIPS生成的思路吧，这是我对我的编译器框架最不满的地方，充满了特判，虽然我能想清楚，但是确实是缺乏健壮性的
    是两种情况：
    1是indexs序列长度为1, gep ty ptr off 这种形式，相当于 ans = ptr + off，
    但是呢，MIPS的内存是连续的，因为这种情况下的ptr类型只能是一个数组指针或者指针，而off就是他的子集元素的长度乘以4

    2是index序列长度是2，其实这个第一个只能是0，所以其实不用处理。。。。因为这个类型是一个数组指针，第一个index就相当于把ptr的寄存器或者内存中的
    地址取出来，存在临时寄存器里面，这个值是一个地址，然后就是该地址加上off，这个off就是index2乘以他的elementType的长度乘以4

    .。就这样先
     */
    @Override
    public void genMIPS() {
        LLVM_Value ptr = operands.get(0);
        ArrayList<LLVM_Value> indexs = new ArrayList<>(this.operands.subList(1, this.operands.size()));
        CommentAsm asm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(asm);
        RegDispatcher.getInstance().distributeRegFor(this);
        Register tar;
        if (useReg) {
            tar = register;
        } else {
            tar = Register.K0;
        }
        if (indexs.size() == 1) {
            assert type instanceof PointerType;
            int off = ((PointerType) type).getElementType().getLen();
            Register r1;
            if (indexs.get(0) instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                MoveAsm move = new MoveAsm(tar, r0);
                MipsBuilder.getInstance().addAsm(move);
            } else if (indexs.get(0) instanceof ConstInteger constInteger) {
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                AluITAsm add = new AluITAsm(AluITAsm.Op.addiu, tar, r0, constInteger.getVal() * off);
                MipsBuilder.getInstance().addAsm(add);
            } else {
                LiAsm li = new LiAsm(Register.K0, off);
                MipsBuilder.getInstance().addAsm(li);
                if (indexs.get(0).isUseReg()) {
                    r1 = indexs.get(0).getRegister();
                } else if (indexs.get(0) instanceof ConstInteger constInteger) {
                    r1 = Register.K1;
                    LiAsm li1 = new LiAsm(Register.K1, constInteger.getVal());
                    MipsBuilder.getInstance().addAsm(li1);
                } else {
                    r1 = Register.K1;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r1, Register.SP, indexs.get(0).getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                MulDivAsm mult = new MulDivAsm(MulDivAsm.Op.mult, r1, Register.K0);
                MipsBuilder.getInstance().addAsm(mult);
                HLAsm mflo = new HLAsm(HLAsm.Op.mflo, Register.K1);
                MipsBuilder.getInstance().addAsm(mflo);
                r1 = Register.K1;
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                AluRTAsm add = new AluRTAsm(AluRTAsm.Op.addu, tar, r0, r1);
                MipsBuilder.getInstance().addAsm(add);
            }
            if (!useReg) {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, tar, Register.SP, offset);
                MipsBuilder.getInstance().addAsm(sw);
            }
        } else {
            int l1 = ((PointerType) ptr.getType()).getElementType().getLen();//通常是0,就不算了
            int l2 = ((ArrayType) ((PointerType) ptr.getType()).getElementType()).getElementType().getLen();
            Register r1;
            Register r2;
            if (indexs.get(1) instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                r2 = Register.ZERO;
                MoveAsm moveAsm = new MoveAsm(tar, r0);
                MipsBuilder.getInstance().addAsm(moveAsm);
            } else if (indexs.get(1) instanceof ConstInteger constInteger) {
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                AluITAsm addiu = new AluITAsm(AluITAsm.Op.addiu, tar, r0, constInteger.getVal() * l2);
                MipsBuilder.getInstance().addAsm(addiu);
            } else {
                if (indexs.get(1).isUseReg()) {
                    r2 = indexs.get(1).getRegister();
                }  else {
                    r2 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r2, Register.SP, indexs.get(1).getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                //然后计算l2 * index2
                LiAsm li = new LiAsm(Register.K1, l2);
                MipsBuilder.getInstance().addAsm(li);
                MulDivAsm mult = new MulDivAsm(MulDivAsm.Op.mult, r2, Register.K1);
                MipsBuilder.getInstance().addAsm(mult);
                HLAsm mflo = new HLAsm(HLAsm.Op.mflo, Register.K1);
                MipsBuilder.getInstance().addAsm(mflo);
                r2 = Register.K1;
                Register r0;
                if (ptr.getName().charAt(0) == '@') {
                    r0 = Register.K0;
                    LaAsm la = new LaAsm(r0, new LabelAsm("global_" + ptr.getName().substring(1)));
                    MipsBuilder.getInstance().addAsm(la);
                } else if (ptr.isUseReg()) {
                    r0 = ptr.getRegister();
                } else {
                    r0 = Register.K0;
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r0, Register.SP, ptr.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                }
                //把数组的基地址取出来存到K0
                AluRTAsm add = new AluRTAsm(AluRTAsm.Op.addu, tar, r0, r2);
                MipsBuilder.getInstance().addAsm(add);
            }
            //TODO:将K0存进去
            if (!useReg) {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, tar, Register.SP, offset);
                MipsBuilder.getInstance().addAsm(sw);
            }
        }
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        if (operands.size() == 2) {
            return new GEPInstr(operands.get(0).copy(map), operands.get(1).copy(map));
        } else {
            return new GEPInstr(operands.get(0).copy(map), (ConstInteger) operands.get(1), operands.get(2).copy(map));
        }
    }

    @Override
    public String GVNHash() {
        StringBuilder sb = new StringBuilder();
        if (operands.size() == 2)
            sb.append("GEP").append(operands.get(0).hash).append(operands.get(1).hash);
        else sb.append("GEP").append(operands.get(0).hash).append(operands.get(1).hash).append(operands.get(2).hash);
        return sb.toString();
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
