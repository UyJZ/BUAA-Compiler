package FrontEnd.Lexer;

public class Token {

    private final String value;

    private TokenType type;

    private final int line;

    public Token(String token, TokenType type, int line) {
        this.value = token;
        this.type = type;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
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

    public enum TokenType {
        IDENFR, INTCON, STRCON, MAINTK, CONSTTK, INTTK, BREAKTK, CONTINUETK, IFTK, ELSETK, NOT, AND, OR, FORTK,
        GETINTTK, PRINTFTK, RETURNTK, PLUS, MINU, VOIDTK, ASSIGN, SEMICN, COMMA, LPARENT, RPARENT, LBRACK, RBRACK,
        LBRACE, RBRACE, NEQ, EQL, GEQ, GRE, LEQ, LSS, MOD, DIV, MULT
    }
}
