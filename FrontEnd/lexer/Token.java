package FrontEnd.lexer;

public class Token {

    private final String value;

    private tokenType type;

    public Token(String token, tokenType type) {
        this.value = token;
        this.type = type;
    }

    @Override
    public String toString() {
        return  type.toString() + " " + value;
    }
}
