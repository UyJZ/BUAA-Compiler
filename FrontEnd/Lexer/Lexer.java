package FrontEnd.Lexer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final StringBuilder source;

    private static final String spaces = "\0\n\t ";

    private final ArrayList<Token> tokens;

    private final TokenStream tokenStream;

    private static final Map<String, Token.TokenType> OPERATOR_MAP = new LinkedHashMap<>();

    static {
        OPERATOR_MAP.put("&&", Token.TokenType.AND);
        OPERATOR_MAP.put("||", Token.TokenType.OR);
        OPERATOR_MAP.put("<=", Token.TokenType.LEQ);
        OPERATOR_MAP.put(">=", Token.TokenType.GEQ);
        OPERATOR_MAP.put("==", Token.TokenType.EQL);
        OPERATOR_MAP.put("!=", Token.TokenType.NEQ);
        OPERATOR_MAP.put("!", Token.TokenType.NOT);
        OPERATOR_MAP.put("+", Token.TokenType.PLUS);
        OPERATOR_MAP.put("-", Token.TokenType.MINU);
        OPERATOR_MAP.put("*", Token.TokenType.MULT);
        OPERATOR_MAP.put("/", Token.TokenType.DIV);
        OPERATOR_MAP.put("%", Token.TokenType.MOD);
        OPERATOR_MAP.put("<", Token.TokenType.LSS);
        OPERATOR_MAP.put(">", Token.TokenType.GRE);
        OPERATOR_MAP.put("=", Token.TokenType.ASSIGN);
        OPERATOR_MAP.put(";", Token.TokenType.SEMICN);
        OPERATOR_MAP.put(",", Token.TokenType.COMMA);
        OPERATOR_MAP.put("(", Token.TokenType.LPARENT);
        OPERATOR_MAP.put(")", Token.TokenType.RPARENT);
        OPERATOR_MAP.put("[", Token.TokenType.LBRACK);
        OPERATOR_MAP.put("]", Token.TokenType.RBRACK);
        OPERATOR_MAP.put("{", Token.TokenType.LBRACE);
        OPERATOR_MAP.put("}", Token.TokenType.RBRACE);
    }

    private int pos;

    private int curLine;

    public Lexer(StringBuilder source) {
        this.source = new StringBuilder(source);
        pos = 0;
        curLine = 1;
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(new ArrayList<>());
    }

    public void next() {
        pos++;
    }

    private void skipSpace() {
        while (hasNext() && spaces.contains(source.substring(pos, pos + 1))) {
            if (source.charAt(pos) == '\n') {
                curLine++;
            }
            next();
        }
    }

    public boolean hasNext() {
        return pos < source.length();
    }

    private String nextString(int length) {
        return source.substring(pos, Math.min(source.length(), pos + length));
    }

    private void skipComment() {
        if (nextString(2).equals("//")) {
            while (hasNext()) {
                if (source.substring(pos, pos + 1).equals("\n")) {
                    pos++;
                    curLine++;
                    break;
                }
                next();
            }
        } else if (nextString(2).equals("/*")) {
            pos+=2;
            while (hasNext()) {
                if (source.substring(pos, pos + 1).equals("\n")) curLine++;
                if (nextString(2).equals("*/")) {
                    pos += 2;
                    break;
                }
                next();
            }
        }
    }

    private Token.TokenType recognizeKeyWords(String s) {
        Pattern patternIdent = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        Matcher matcherIdent = patternIdent.matcher(s);
        if (!matcherIdent.matches()) {
            System.out.println("ERROR!");
        }
        return switch (s) {
            case "const" -> Token.TokenType.CONSTTK;
            case "main" -> Token.TokenType.MAINTK;
            case "int" -> Token.TokenType.INTTK;
            case "break" -> Token.TokenType.BREAKTK;
            case "continue" -> Token.TokenType.CONTINUETK;
            case "if" -> Token.TokenType.IFTK;
            case "else" -> Token.TokenType.ELSETK;
            case "for" -> Token.TokenType.FORTK;
            case "getint" -> Token.TokenType.GETINTTK;
            case "printf" -> Token.TokenType.PRINTFTK;
            case "return" -> Token.TokenType.RETURNTK;
            case "void" -> Token.TokenType.VOIDTK;
            default -> Token.TokenType.IDENFR;
        };
    }

    private Token recognizeSymbol() {
        String s = source.substring(pos);
        for (Map.Entry<String, Token.TokenType> entry : OPERATOR_MAP.entrySet()) {
            if (s.startsWith(entry.getKey())) {
                pos += entry.getKey().length();
                return new Token(entry.getKey(), entry.getValue(), curLine);
            }
        }
        return null;
    }

    private Token recognizeIdent() {
        int l = 1;
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        while (pos + l <= source.length()) {
            Matcher matcher = pattern.matcher(nextString(l));
            if (!matcher.matches()) {
                l--;
                break;
            }
            l++;
        }
        String s = nextString(l);
        pos = pos + l;
        return new Token(s, recognizeKeyWords(s), curLine);
    }

    private boolean isIntCon(String s) {
        Pattern patternIntCon = Pattern.compile("[0-9]+");
        Matcher matcher = patternIntCon.matcher(s);
        return matcher.matches();
    }

    private Token recognizeIntCon() {
        Pattern patternIntCon = Pattern.compile("[+-]?[0-9]+");
        int l = 2;
        while (pos + l <= source.length()) {
            if (!patternIntCon.matcher(nextString(l)).matches()) {
                l--;
                String s = source.substring(pos, pos + l);
                pos = pos + l;
                return new Token(s, Token.TokenType.INTCON, curLine);
            }
            l++;
        }
        return null;
    }

    private Token recognizeFormatString() {
        int l = 1;
        while (pos + l <= source.length()) {
            if (source.charAt(pos + l) == '"') {
                Token token = new Token(source.substring(pos, pos + l + 1), Token.TokenType.STRCON, curLine);
                pos = pos + l + 1;
                return token;
            }
            l++;
        }
        return null;
    }

    public Token nextToken() {
        Pattern patternIdentHead = Pattern.compile("[a-zA-Z_]");
        String symbolsHead = "!|&+-*/%<>=;,()[]{}";
        skipSpace();
        while (hasNext()) {
            if (nextString(2).equals("//") || nextString(2).equals("/*")) {
                skipComment();
            } else if (source.charAt(pos) == '"') {
                return recognizeFormatString();
            } else if (patternIdentHead.matcher(nextString(1)).matches()) {
                return recognizeIdent();
            } else if (isIntCon(nextString(1))) {
                return recognizeIntCon();
            } else if (symbolsHead.contains(source.substring(pos, pos + 1))) {
                return recognizeSymbol();
            } else skipSpace();
        }
        return null;
    }

    public void analysisLexer() {
        while (true) {
            Token token = nextToken();
            if (token == null) {
                break;
            }
            tokens.add(token);
            tokenStream.addToken(token);
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public TokenStream getTokenStream() {
        return tokenStream;
    }

}
