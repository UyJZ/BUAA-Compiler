package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.AluITAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import MidEnd.RegDispatcher;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class PushMarco {
    private final ArrayList<Register> operands;

    public PushMarco(LinkedHashSet<Register> operands) {
        this.operands = new ArrayList<>();
        this.operands.addAll(operands);
    }

    public void addToMipsController() {
        for (Register register : operands) {
            RegDispatcher.getInstance().allocSpaceForReg();
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, register, Register.SP, RegDispatcher.getInstance().getCurrentOffset());
            MipsController.getInstance().addAsm(sw);
        }
        AluITAsm addi = new AluITAsm(AluITAsm.Op.addi, Register.SP, Register.SP, RegDispatcher.getInstance().getCurrentOffset());
        MipsController.getInstance().addAsm(addi);
    }
}
