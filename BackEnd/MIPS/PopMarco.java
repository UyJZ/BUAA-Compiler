package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.MemITAsm;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class PopMarco {
    private final ArrayList<Register> operands;

    public PopMarco(LinkedHashSet<Register> operands) {
        this.operands = new ArrayList<>();
        this.operands.addAll(operands);
    }

    public void addToMipsController() {
        for (int i = 0; i < operands.size(); i++) {
            Register register = operands.get(i);
            MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, register, Register.SP, 4 * (operands.size() - 1 - i));
            MipsBuilder.getInstance().addAsm(lw);
        }
    }
}
