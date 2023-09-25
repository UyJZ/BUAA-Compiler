package FrontEnd.lexer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final StringBuilder source;

    private static final String spaces = "\0\n\t ";

    private final ArrayList<Token> tokens;

    private static final Map<String, tokenType> OPERATOR_MAP = new LinkedHashMap<>();

    static {
        OPERATOR_MAP.put("&&", tokenType.AND);
        OPERATOR_MAP.put("||", tokenType.OR);
        OPERATOR_MAP.put("<=", tokenType.LEQ);
        OPERATOR_MAP.put(">=", tokenType.GEQ);
        OPERATOR_MAP.put("==", tokenType.EQL);
        OPERATOR_MAP.put("!=", tokenType.NEQ);
        OPERATOR_MAP.put("!", tokenType.NOT);
        OPERATOR_MAP.put("+", tokenType.PLUS);
        OPERATOR_MAP.put("-", tokenType.MINU);
        OPERATOR_MAP.put("*", tokenType.MULT);
        OPERATOR_MAP.put("/", tokenType.DIV);
        OPERATOR_MAP.put("%", tokenType.MOD);
        OPERATOR_MAP.put("<", tokenType.LSS);
        OPERATOR_MAP.put(">", tokenType.GRE);
        OPERATOR_MAP.put("=", tokenType.ASSIGN);
        OPERATOR_MAP.put(";", tokenType.SEMICN);
        OPERATOR_MAP.put(",", tokenType.COMMA);
        OPERATOR_MAP.put("(", tokenType.LPARENT);
        OPERATOR_MAP.put(")", tokenType.RPARENT);
        OPERATOR_MAP.put("[", tokenType.LBRACK);
        OPERATOR_MAP.put("]", tokenType.RBRACK);
        OPERATOR_MAP.put("{", tokenType.LBRACE);
        OPERATOR_MAP.put("}", tokenType.RBRACE);
    }

    private int pos;

    public Lexer(StringBuilder source) {
        this.source = new StringBuilder(source);
        pos = 0;
        lines = 0;
        tokens = new ArrayList<>();
    }

    private int lines;

    public void next() {
        pos++;
    }

    private void skipSpace() {
        while (hasNext() && spaces.contains(source.substring(pos, pos + 1))) {
            if (source.charAt(pos) == '\n') {
                lines++;
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
                    break;
                }
                next();
            }
        } else if (nextString(2).equals("/*")) {
            pos++;
            while (hasNext()) {
                if (nextString(2).equals("*/")) {
                    pos += 2;
                    break;
                }
                next();
            }
        }
    }

    private tokenType recognizeKeyWords(String s) {
        Pattern patternIdent = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        Matcher matcherIdent = patternIdent.matcher(s);
        if (!matcherIdent.matches()) {
            System.out.println("ERROR!");
        }
        return switch (s) {
            case "const" -> tokenType.CONSTTK;
            case "main" -> tokenType.MAINTK;
            case "int" -> tokenType.INTTK;
            case "break" -> tokenType.BREAKTK;
            case "continue" -> tokenType.CONTINUETK;
            case "if" -> tokenType.IFTK;
            case "else" -> tokenType.ELSETK;
            case "for" -> tokenType.FORTK;
            case "getint" -> tokenType.GETINTTK;
            case "printf" -> tokenType.PRINTFTK;
            case "return" -> tokenType.RETURNTK;
            case "void" -> tokenType.VOIDTK;
            default -> tokenType.IDENFR;
        };
    }

    private Token recognizeSymbol() {
        String s = source.substring(pos);
        for (Map.Entry<String, tokenType> entry : OPERATOR_MAP.entrySet()) {
            if (s.startsWith(entry.getKey())) {
                pos += entry.getKey().length();
                return new Token(entry.getKey(), entry.getValue());
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
        return new Token(s, recognizeKeyWords(s));
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
                return new Token(s, tokenType.INTCON);
            }
            l++;
        }
        return null;
    }

    private Token recognizeFormatString() {
        int l = 1;
        while (pos + l <= source.length()) {
            if (source.charAt(pos + l) == '"') {
                Token token = new Token(source.substring(pos, pos + l + 1), tokenType.STRCON);
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

    public ArrayList<Token> analysisLexer() {
        while (true) {
            Token token = nextToken();
            if (token == null) {
                break;
            }
            tokens.add(token);
        }
        return tokens;
    }

}
