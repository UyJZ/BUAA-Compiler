package BackEnd.MIPS.Assembly;

public class LabelAsm extends Asm {

    private String name;

    public LabelAsm(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name + ":";
    }

    public String getName() {
        return name;
    }
}
