package BackEnd.MIPS.Assembly;

public class JalAsm extends Asm {
    private LabelAsm label;

    public JalAsm(LabelAsm label) {
        super();
        this.label = label;
    }
}
