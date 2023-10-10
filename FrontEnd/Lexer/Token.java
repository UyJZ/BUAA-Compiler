package FrontEnd.Lexer;

import Enums.tokenType;

public class Token {

    private final String value;

    private tokenType type;

    public Token(String token, tokenType type) {
        this.value = token;
        this.type = type;
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
}
