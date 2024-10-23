package Ir_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LiAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.Assembly.MoveAsm;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Ir_LLVM.LLVM_Value;
import MidEnd.RegDispatcher;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Types.VoidType;

public class MoveInstr extends Instr {
    public MoveInstr(LLVM_Value dst, LLVM_Value src) {
        super(new VoidType(), "");
        addValue(dst);
        addValue(src);
    }

    @Override
    public void setName() {
        for (LLVM_Value LLVMValue : operands) {
            if (LLVMValue.getName().length() == 0) {
                LLVMValue.setName(LLVM_Builder.getInstance().genVirtualRegNum());
            }
        }
    }

    public void clearName() {
        for (LLVM_Value LLVMValue : operands) {
            LLVMValue.setName("");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return "move " + " " + operands.get(0).getName() + ", " + operands.get(1).getName();
        /*
        sb.append(operands.get(0).getName() + " = " + "add " + operands.get(1).getType() + " " + operands.get(1).getName() + ", 0");
        if (operands.get(0) instanceof TempValue) {
            sb.append(" ; temp");
        }
        return sb.toString();

         */
    }

    @Override
    public void genMIPS() {
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(commentAsm);
        Register r1;
        Register r2;
        if (operands.get(0).getName().equals("%680")) {
            System.out.println("debug");
        }
        if (!(operands.get(0) instanceof ConstInteger)) {
            RegDispatcher.getInstance().distributeRegFor(operands.get(0));
        }
        if (!(operands.get(1) instanceof ConstInteger)) {
            RegDispatcher.getInstance().distributeRegFor(operands.get(1));
        }
        if (operands.get(0).isUseReg()) {
            r1 = operands.get(0).getRegister();
            if (operands.get(1).isUseReg()) {
                r2 = operands.get(1).getRegister();
                if (r1 == r2) return;
                MoveAsm move = new MoveAsm(r1, r2);
                MipsBuilder.getInstance().addAsm(move);
            } else if (operands.get(1) instanceof ConstInteger) {
                LiAsm li = new LiAsm(r1, ((ConstInteger) operands.get(1)).getVal());
                MipsBuilder.getInstance().addAsm(li);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r1, Register.SP, operands.get(1).getOffset());
                MipsBuilder.getInstance().addAsm(lw);
            }
        } else {
            if (operands.get(1).isUseReg()) {
                r2 = operands.get(1).getRegister();
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, r2, Register.SP, operands.get(0).getOffset());
                MipsBuilder.getInstance().addAsm(sw);
            } else if (operands.get(1) instanceof ConstInteger constInteger) {
                LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
                MipsBuilder.getInstance().addAsm(li);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, operands.get(0).getOffset());
                MipsBuilder.getInstance().addAsm(sw);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operands.get(1).getOffset());
                MipsBuilder.getInstance().addAsm(lw);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, operands.get(0).getOffset());
                MipsBuilder.getInstance().addAsm(sw);
            }
        }
    }

    @Override
    public boolean isDefinition() {
        return true;
    }

    public LLVM_Value getDst() {
        return operands.get(0);
    }

    public LLVM_Value getSrc() {
        return operands.get(1);
    }
}
