package MyPackage.Parse;

import java.util.ArrayList;

public class FuncFParams {
    private ArrayList<FuncFParam> funcFParams;

    public FuncFParams(ArrayList<FuncFParam> funcFParams) {
        this.funcFParams = funcFParams;
    }

    public int getNumber() {
        return funcFParams.size();
    }

    public int getLevel(int index) {
        return funcFParams.get(index).getLevel();
    }

    public void generateLlvm() {
        for (int i = 0; i < funcFParams.size(); i++) {
            funcFParams.get(i).generateLlvm();
        }
    }
}
