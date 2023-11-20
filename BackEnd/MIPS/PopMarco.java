package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.AluITAsm;
import BackEnd.MIPS.Assembly.MemITAsm;
import MidEnd.RegDispatcher;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

public class PopMarco {
    private final ArrayList<Register> operands;

    public PopMarco(LinkedHashSet<Register> operands, int offset) {
        this.operands = new ArrayList<>();
        this.operands.addAll(operands);
        Collections.reverse(this.operands);
    }

    public void addToMipsController() {
        for (int i = 0; i < operands.size(); i++) {
            Register register = operands.get(i);
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, register, Register.SP, 4 * i);
            MipsController.getInstance().addAsm(lw);
        }
    }
}
