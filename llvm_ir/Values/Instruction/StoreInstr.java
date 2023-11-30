package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.HashMap;

public class StoreInstr extends Instr {

    private int val;

    private LLVMType type2;

    public StoreInstr(Value from, Value to) {
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
        MipsController.getInstance().addAsm(commentAsm);
        Register fromReg;
        Register toReg;
        Value from = operands.get(0);
        Value to = operands.get(1);
        if (from.isUseReg()) {
            fromReg = from.getRegister();
        } else if (from instanceof ConstInteger constInteger) {
            fromReg = Register.K0;
            val = constInteger.getVal();
            LiAsm li = new LiAsm(Register.K0, val);
            MipsController.getInstance().addAsm(li);
        } else {
            fromReg = Register.K0;
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, from.getOffset());
            MipsController.getInstance().addAsm(lw);
        }
        if (to.getName().charAt(0) == '@') {
            toReg = Register.K1;
            LaAsm la = new LaAsm(Register.K1, new LabelAsm("global_" + to.getName().substring(1)));
            MipsController.getInstance().addAsm(la);
        } else {
            if (to.isUseReg()) {
                toReg = to.getRegister();
            } else {
                toReg = Register.K1;
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, to.getOffset());
                MipsController.getInstance().addAsm(lw);
            }
        }
        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, fromReg, toReg, 0);
        MipsController.getInstance().addAsm(sw);
    }

    public Value getDst() {
        return operands.get(1);
    }

    public Value getSrc() {
        return operands.get(0);
    }

    @Override
    public Instr copy(HashMap<Value, Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new StoreInstr(operands.get(0).copy(map), operands.get(1).copy(map));
    }
}
