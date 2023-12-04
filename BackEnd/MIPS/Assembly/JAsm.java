package BackEnd.MIPS.Assembly;

public class JAsm extends Asm{
    private LabelAsm label;

    public JAsm(LabelAsm label) {
        super();
        this.label = label;
    }

    @Override
    public String toString() {
        return "\tj\t" + label.getName();
    }
}
