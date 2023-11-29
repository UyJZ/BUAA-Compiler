package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.AluRTAsm;
import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LiAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import Config.tasks;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.BoolType;

public class IcmpInstr extends Instr {

    public enum CmpOp {
        eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
    }

    private final CmpOp opcode;

    public IcmpInstr(Value operand1, Value operand2, CmpOp cmpOp) {
        super(new BoolType(), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
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
        Value operand1 = operands.get(0);
        Value operand2 = operands.get(1);
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(commentAsm);
        Register r1;
        Register r2;
        if (operand1.isUseReg()) {
            r1 = operand1.getRegister();
        } else if (operand1 instanceof ConstInteger constInteger) {
            r1 = Register.K0;
            LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
            MipsController.getInstance().addAsm(li);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand1.getOffset());
            MipsController.getInstance().addAsm(lw);
            r1 = Register.K0;
        }
        if (operand2.isUseReg()) {
            r2 = operand2.getRegister();
        } else if (operand2 instanceof ConstInteger) {
            r2 = Register.K1;
            LiAsm li = new LiAsm(Register.K1, ((ConstInteger) operand2).getVal());
            MipsController.getInstance().addAsm(li);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, operand2.getOffset());
            MipsController.getInstance().addAsm(lw);
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
            MipsController.getInstance().addAsm(asm);
        } else {
            AluRTAsm asm = new AluRTAsm(op, Register.K0, r1, r2);
            MipsController.getInstance().addAsm(asm);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, offset);
            MipsController.getInstance().addAsm(sw);
        }
    }
}
