package MyPackage.Symbol;


import java.util.ArrayList;

public class Symbol {
    private String name;
    private boolean isConst;
    private int addr;
    private int line;
    private String type;

    public Symbol(String name, int line, boolean isConst, String type) {
        this.name = name;
        this.line = line;
        this.isConst = isConst;
        this.type = type;
    }

    public boolean isConst() {
        return isConst;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
