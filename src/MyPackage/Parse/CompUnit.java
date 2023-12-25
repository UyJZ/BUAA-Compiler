package MyPackage.Parse;


import MyPackage.IR.IRModule;


import MyPackage.IR.Value;


import java.util.ArrayList;


public class CompUnit {
    final private ArrayList<Decl> list;
    public CompUnit(ArrayList<Decl> list) {
        this.list = list;
    }

    public void generateLlvm() {
        ArrayList<Value> decls = IRModule.getDecls();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).generateLlvm(true);
        }
    }
}
