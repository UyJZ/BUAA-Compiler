package IR_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Types.LLVMType;
import IR_LLVM.LLVM_Types.VoidType;

import java.util.HashMap;

public class StoreInstr extends Instr {

    private int val;

    private LLVMType type2;

    public StoreInstr(LLVM_Value from, LLVM_Value to) {
        super(new VoidType(), "");
        this.addValue(from);
        this.addValue(to);
    }

    @Override
    public String toString() {
        return "store " + operands.get(0).getType().toString() + " " + operands.get(0).getName() + ", " + operands.get(1).getType().toString() + " " + operands.get(1).getName();
    }

    @Override
    public void genMIPS() {
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(commentAsm);
        Register fromReg;
        Register toReg;
        LLVM_Value from = operands.get(0);
        LLVM_Value to = operands.get(1);
        if (from.isUseReg()) {
            fromReg = from.getRegister();
        } else if (from instanceof ConstInteger constInteger) {
            fromReg = Register.K0;
            val = constInteger.getVal();
            LiAsm li = new LiAsm(Register.K0, val);
            MipsBuilder.getInstance().addAsm(li);
        } else {
            fromReg = Register.K0;
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, from.getOffset());
            MipsBuilder.getInstance().addAsm(lw);
        }
        if (to.getName().charAt(0) == '@') {
            toReg = Register.K1;
            LaAsm la = new LaAsm(Register.K1, new LabelAsm("global_" + to.getName().substring(1)));
            MipsBuilder.getInstance().addAsm(la);
        } else {
            if (to.isUseReg()) {
                toReg = to.getRegister();
            } else {
                toReg = Register.K1;
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, to.getOffset());
                MipsBuilder.getInstance().addAsm(lw);
            }
        }
        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, fromReg, toReg, 0);
        MipsBuilder.getInstance().addAsm(sw);
    }

    public LLVM_Value getDst() {
        return operands.get(1);
    }

    public LLVM_Value getSrc() {
        return operands.get(0);
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new StoreInstr(operands.get(0).copy(map), operands.get(1).copy(map));
    }
}
