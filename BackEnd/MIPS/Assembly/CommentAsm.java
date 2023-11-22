package BackEnd.MIPS.Assembly;

public class CommentAsm extends Asm{
    public String comment;

    public CommentAsm(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "\t# " + comment;
    }
}
