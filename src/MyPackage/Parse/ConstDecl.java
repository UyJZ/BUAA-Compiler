package MyPackage.Parse;


import java.util.ArrayList;

public class ConstDecl implements Decl {
    private String type;
    private ArrayList<ConstDef> list;
    public ConstDecl(String type, ArrayList<ConstDef> list) {
        this.list = list;
        this.type = type;
    }

    public void generateLlvm(boolean isGlobal) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).generateLlvm(isGlobal);
        }
    }
}
