package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class LaAsm extends Asm {
    private final Register rt;

    private final LabelAsm label;

    public LaAsm(Register rt, LabelAsm label) {
        this.rt = rt;
        this.label = label;
    }

    @Override
    public String toString() {
        return "\tla\t" + rt + ", " + label.getName();
    }
}
