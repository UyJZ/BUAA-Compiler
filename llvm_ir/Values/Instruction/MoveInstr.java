package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LiAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.Assembly.MoveAsm;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.VoidType;

public class MoveInstr extends Instr {
    public MoveInstr(Value dst, Value src) {
        super(new VoidType(), "");
        addValue(dst);
        addValue(src);
    }

    @Override
    public void setName() {
        for (Value value : operands) {
            if (value.getName().length() == 0) {
                value.setName(IRController.getInstance().genVirtualRegNum());
            }
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
        MipsController.getInstance().addAsm(commentAsm);
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
                MipsController.getInstance().addAsm(move);
            } else if (operands.get(1) instanceof ConstInteger) {
                LiAsm li = new LiAsm(r1, ((ConstInteger) operands.get(1)).getVal());
                MipsController.getInstance().addAsm(li);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, r1, Register.SP, operands.get(1).getOffset());
                MipsController.getInstance().addAsm(lw);
            }
        } else {
            if (operands.get(1).isUseReg()) {
                r2 = operands.get(1).getRegister();
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, r2, Register.SP, operands.get(0).getOffset());
                MipsController.getInstance().addAsm(sw);
            } else if (operands.get(1) instanceof ConstInteger constInteger) {
                LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
                MipsController.getInstance().addAsm(li);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, operands.get(0).getOffset());
                MipsController.getInstance().addAsm(sw);
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operands.get(1).getOffset());
                MipsController.getInstance().addAsm(lw);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, operands.get(0).getOffset());
                MipsController.getInstance().addAsm(sw);
            }
        }
    }

    @Override
    public boolean isDefinition() {
        return true;
    }

    public Value getDst() {
        return operands.get(0);
    }

    public Value getSrc() {
        return operands.get(1);
    }
}
