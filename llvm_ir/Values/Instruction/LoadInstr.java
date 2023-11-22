package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.CommentAsm;
import BackEnd.MIPS.Assembly.LaAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import BackEnd.MIPS.Assembly.MoveAsm;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import Config.tasks;
import FrontEnd.Symbol.VarSymbol;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.GlobalVar;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

public class LoadInstr extends Instr {

    private Value ptr;

    public LoadInstr(Value ptr) {
        super(((PointerType) ptr.getType()).getElementType(), tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.ptr = ptr;
        this.operands.add(ptr);
    }

    @Override
    public String toString() {
        return name + " = load " + type + " , " + type + "* " + ptr.getName();
    }

    @Override
    public void genMIPS() {
        CommentAsm commentAsm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(commentAsm);
        RegDispatcher.getInstance().distributeRegFor(this);
        if (ptr instanceof GlobalVar globalVar) {
            LaAsm la = new LaAsm(Register.K0, globalVar.getLabel());
            MipsController.getInstance().addAsm(la);
            if (globalVar.getDim() == 0 && useReg) {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, this.register, Register.K0, 0);
                MipsController.getInstance().addAsm(lw);
            } else if (globalVar.getDim() == 0 && !useReg) {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.K0, 0);
                MipsController.getInstance().addAsm(lw);
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
                MipsController.getInstance().addAsm(sw);
            } else if (globalVar.getDim() != 0 && useReg) {
                MoveAsm move = new MoveAsm(this.register, Register.K0);
                MipsController.getInstance().addAsm(move);
            } else {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
                MipsController.getInstance().addAsm(sw);
            }
        } else if (ptr.isUseReg() && useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, this.register, ptr.getRegister(), 0);
            MipsController.getInstance().addAsm(lw);
        } else if (ptr.isUseReg() && !useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, ptr.getRegister(), 0);
            MipsController.getInstance().addAsm(lw);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
        } else if (!ptr.isUseReg() && useReg) {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, ptr.getOffset());
            MipsController.getInstance().addAsm(lw);
            MemITAsm lw1 = new MemITAsm(MemITAsm.Op.lw, this.register, Register.K0, 0);
            MipsController.getInstance().addAsm(lw1);
        } else {
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, ptr.getOffset());
            MipsController.getInstance().addAsm(lw);
            MemITAsm lw1 = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.K0, 0);
            MipsController.getInstance().addAsm(lw1);
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K1, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
        }
    }

}
