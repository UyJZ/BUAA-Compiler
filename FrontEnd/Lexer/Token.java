package FrontEnd.Lexer;

import Enums.tokenType;

public class Token {

    private final String value;

    private tokenType type;

    private final int line;

    public Token(String token, tokenType type, int line) {
        this.value = token;
        this.type = type;
        this.line = line;
    }

    public tokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString() + " " + value;
    }

    public String typeString() {
        return type.toString();
    }

    public int getLine() {
        return line;
    }
}
