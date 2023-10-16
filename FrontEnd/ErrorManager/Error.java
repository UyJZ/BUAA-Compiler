package FrontEnd.ErrorManager;

import Enums.ErrorType;

public class Error implements Comparable<Object>{
    private final int line;

    private final ErrorType type;

    public Error(int line, ErrorType type) {
        this.line = line;
        this.type = type;
    }

    @Override
    public int compareTo(Object o) {
        Error e = (Error) o;
        if (this.line == e.line) return this.type.ordinal() - e.type.ordinal();
        return this.line - e.line;
    }

    @Override
    public String toString() {
        return line + " " + type;
    }
}
