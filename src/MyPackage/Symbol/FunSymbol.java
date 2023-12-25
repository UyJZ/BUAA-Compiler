package MyPackage.Symbol;

import MyPackage.Parse.FuncFParams;

import java.util.ArrayList;

public class FunSymbol extends Symbol{

    FuncFParams funcFParams;

    public FunSymbol(String name, int line, boolean isConst, String type, FuncFParams funcFParams) {
        super(name, line, isConst, type);
        this.funcFParams = funcFParams;
    }

    public int getNumber() {
        if (funcFParams == null) {
            return 0;
        }
        return funcFParams.getNumber();
    }

    public int getLevel(int index) {
        return funcFParams.getLevel(index);
    }

}

