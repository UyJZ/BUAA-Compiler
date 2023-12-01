package llvm_ir.Values.Instruction.terminatorInstr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.HashMap;

public class ReturnInstr extends Instr {
    public ReturnInstr(LLVMType type) {
        super(type, "");
    }

    public ReturnInstr(LLVMType type, Value value) {
        super(type, value.getName());
        this.addValue(value);
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

    @Override
    public void genMIPS() {
        CommentAsm asm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(asm);
        if (type instanceof VoidType) {
            JrAsm jr = new JrAsm(Register.RA);
            MipsController.getInstance().addAsm(jr);
        } else {
            if (operands.get(0).isUseReg()) {
                MoveAsm move = new MoveAsm(Register.V0, operands.get(0).getRegister());
                MipsController.getInstance().addAsm(move);
            } else if (operands.get(0) instanceof ConstInteger constInteger) {
                LiAsm liAsm = new LiAsm(Register.V0, constInteger.getVal());
                MipsController.getInstance().addAsm(liAsm);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.V0, Register.SP, operands.get(0).getOffset());
                MipsController.getInstance().addAsm(lw);
            }
            JrAsm jr = new JrAsm(Register.RA);
            MipsController.getInstance().addAsm(jr);
        }
    }

    public Value getReturnValue() {
        return operands.get(0);
    }

    @Override
    public Instr copy(HashMap<Value, Value> map) {
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
