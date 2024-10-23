package Ir_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.AluRTAsm;
import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LiAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Config.Tasks;
import Ir_LLVM.LLVM_Value;
import MidEnd.RegDispatcher;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Types.BoolType;

import java.util.HashMap;

public class IcmpInstr extends Instr {

    public enum CmpOp {
        eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
    }

    private final CmpOp opcode;

    public IcmpInstr(LLVM_Value operand1, LLVM_Value operand2, CmpOp cmpOp) {
        super(new BoolType(), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        this.opcode = cmpOp;
        this.addValue(operand1);
        this.addValue(operand2);
    }

    @Override
    public String toString() {
        return name + " = " + "icmp " + opcode.toString() + " " + operands.get(0).getType().toString() + " " + operands.get(0).getName() + ", " + operands.get(1).getName();
    }

    @Override
    public void genMIPS() {
        LLVM_Value operand1 = operands.get(0);
        LLVM_Value operand2 = operands.get(1);
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(commentAsm);
        Register r1;
        Register r2;
        if (operand1.isUseReg()) {
            r1 = operand1.getRegister();
        } else if (operand1 instanceof ConstInteger constInteger) {
            r1 = Register.K0;
            LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
            MipsBuilder.getInstance().addAsm(li);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand1.getOffset());
            MipsBuilder.getInstance().addAsm(lw);
            r1 = Register.K0;
        }
        if (operand2.isUseReg()) {
            r2 = operand2.getRegister();
        } else if (operand2 instanceof ConstInteger) {
            r2 = Register.K1;
            LiAsm li = new LiAsm(Register.K1, ((ConstInteger) operand2).getVal());
            MipsBuilder.getInstance().addAsm(li);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, operand2.getOffset());
            MipsBuilder.getInstance().addAsm(lw);
            r2 = Register.K1;
        }
        AluRTAsm.Op op = null;
        switch (opcode) {
            case eq -> op = AluRTAsm.Op.seq;
            case ne -> op = AluRTAsm.Op.sne;
            case ugt, sgt -> op = AluRTAsm.Op.sgt;
            case uge, sge -> op = AluRTAsm.Op.sge;
            case ult, slt -> op = AluRTAsm.Op.slt;
            case ule, sle -> op = AluRTAsm.Op.sle;
        }
        RegDispatcher.getInstance().distributeRegFor(this);
        if (useReg) {
            AluRTAsm asm = new AluRTAsm(op, register, r1, r2);
            MipsBuilder.getInstance().addAsm(asm);
        } else {
            AluRTAsm asm = new AluRTAsm(op, Register.K0, r1, r2);
            MipsBuilder.getInstance().addAsm(asm);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, offset);
            MipsBuilder.getInstance().addAsm(sw);
        }
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new IcmpInstr(operands.get(0).copy(map), operands.get(1).copy(map), opcode);
    }

    @Override
    public String GVNHash() {
        StringBuilder sb = new StringBuilder();
        if (opcode == CmpOp.eq || opcode == CmpOp.ne) {
            if (operands.get(0).hash.compareTo(operands.get(1).hash) < 0) {
                sb.append(operands.get(0).hash).append(" ").append(opcode.toString()).append(" ").append(operands.get(1).hash);
            } else {
                sb.append(operands.get(1).hash).append(" ").append(opcode.toString()).append(" ").append(operands.get(0).hash);
            }
        } else if (opcode == CmpOp.sgt || opcode == CmpOp.sge) {
            sb.append(operands.get(0).hash).append(" ").append(opcode.toString()).append(" ").append(operands.get(1).hash);
        } else {
            CmpOp p = (opcode == CmpOp.slt) ? CmpOp.sgt : CmpOp.sge;
            sb.append(operands.get(1).hash).append(" ").append(p.toString()).append(" ").append(operands.get(0).hash);
        }
        return sb.toString();
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
