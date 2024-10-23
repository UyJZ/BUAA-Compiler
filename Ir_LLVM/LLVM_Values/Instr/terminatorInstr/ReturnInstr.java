package Ir_LLVM.LLVM_Values.Instr.terminatorInstr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.Instr;
import Ir_LLVM.LLVM_Types.LLVMType;
import Ir_LLVM.LLVM_Types.VoidType;

import java.util.HashMap;

public class ReturnInstr extends Instr {
    public ReturnInstr(LLVMType type) {
        super(type, "");
    }

    public ReturnInstr(LLVMType type, LLVM_Value LLVMValue) {
        super(type, LLVMValue.getName());
        this.addValue(LLVMValue);
    }

    @Override
    public String toString() {
        if (type instanceof VoidType) {
            return "ret " + type.toString();
        } else {
            return "ret " + type.toString() + " " + operands.get(0).getName();
        }
    }

    public void setName() {
        if (type instanceof VoidType) {
            name = "";
        } else {
            name = operands.get(0).getName();
        }
    }

    public void clearName() {
        name = "";
    }

    @Override
    public void genMIPS() {
        CommentAsm asm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(asm);
        if (type instanceof VoidType) {
            JrAsm jr = new JrAsm(Register.RA);
            MipsBuilder.getInstance().addAsm(jr);
        } else {
            if (operands.get(0).isUseReg()) {
                MoveAsm move = new MoveAsm(Register.V0, operands.get(0).getRegister());
                MipsBuilder.getInstance().addAsm(move);
            } else if (operands.get(0) instanceof ConstInteger constInteger) {
                LiAsm liAsm = new LiAsm(Register.V0, constInteger.getVal());
                MipsBuilder.getInstance().addAsm(liAsm);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.V0, Register.SP, operands.get(0).getOffset());
                MipsBuilder.getInstance().addAsm(lw);
            }
            JrAsm jr = new JrAsm(Register.RA);
            MipsBuilder.getInstance().addAsm(jr);
        }
    }

    public LLVM_Value getReturnValue() {
        return operands.get(0);
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        if (type instanceof VoidType) {
            return new ReturnInstr(type);
        } else {
            return new ReturnInstr(type, operands.get(0).copy(map));
        }
    }

    @Override
    public boolean isPinnedInst() {
        return true;
    }
}
