package IR_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LaAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.Assembly.MoveAsm;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import Config.Tasks;
import IR_LLVM.LLVM_Value;
import MidEnd.RegDispatcher;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.GlobalVar;
import IR_LLVM.LLVM_Types.PointerType;

import java.util.HashMap;

public class LoadInstr extends Instr {

    public LoadInstr(LLVM_Value ptr) {
        super(((PointerType) ptr.getType()).getElementType(), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        this.addValue(ptr);
    }

    @Override
    public String toString() {
        LLVM_Value ptr = operands.get(0);
        return name + " = load " + type + " , " + type + "* " + ptr.getName();
    }

    @Override
    public void genMIPS() {
        LLVM_Value ptr = operands.get(0);
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(commentAsm);
        RegDispatcher.getInstance().distributeRegFor(this);
        if (ptr instanceof GlobalVar globalVar) {
            LaAsm la = new LaAsm(Register.K0, globalVar.getLabel());
            MipsBuilder.getInstance().addAsm(la);
            if (globalVar.getDim() == 0 && useReg) {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, this.register, Register.K0, 0);
                MipsBuilder.getInstance().addAsm(lw);
            } else if (globalVar.getDim() == 0 && !useReg) {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.K0, 0);
                MipsBuilder.getInstance().addAsm(lw);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
                MipsBuilder.getInstance().addAsm(sw);
            } else if (globalVar.getDim() != 0 && useReg) {
                MoveAsm move = new MoveAsm(this.register, Register.K0);
                MipsBuilder.getInstance().addAsm(move);
            } else {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
                MipsBuilder.getInstance().addAsm(sw);
            }
        } else if (ptr.isUseReg() && useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, this.register, ptr.getRegister(), 0);
            MipsBuilder.getInstance().addAsm(lw);
        } else if (ptr.isUseReg() && !useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, ptr.getRegister(), 0);
            MipsBuilder.getInstance().addAsm(lw);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
            MipsBuilder.getInstance().addAsm(sw);
        } else if (!ptr.isUseReg() && useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, ptr.getOffset());
            MipsBuilder.getInstance().addAsm(lw);
            MemITAsm lw1 = new MemITAsm(MemITAsm.Op.lw, this.register, Register.K0, 0);
            MipsBuilder.getInstance().addAsm(lw1);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, ptr.getOffset());
            MipsBuilder.getInstance().addAsm(lw);
            MemITAsm lw1 = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.K0, 0);
            MipsBuilder.getInstance().addAsm(lw1);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K1, Register.SP, this.offset);
            MipsBuilder.getInstance().addAsm(sw);
        }
    }

    public LLVM_Value getPtr() {
        return operands.get(0);
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new LoadInstr(operands.get(0).copy(map));
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
