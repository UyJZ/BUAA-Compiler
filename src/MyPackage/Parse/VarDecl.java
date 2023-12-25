package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Value;

import java.util.ArrayList;

public class VarDecl implements Decl{
    private String type;
    private ArrayList<VarDef> list;
    public VarDecl(String type, ArrayList<VarDef> list) {
        this.list = list;
        this.type = type;
    }

    public void generateLlvm(boolean isGlobal) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).generateLlvm(isGlobal);
        }
    }
}
