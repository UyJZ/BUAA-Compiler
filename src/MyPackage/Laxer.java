package MyPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Laxer {
    private int line;
    private int beforeLine;
    private int pos;
    private int len;
    private String curToken;
    private LexType curLexType;
    final private Scanner scanner;
    private String source;




    public Laxer(File file) {
        curToken = null;
        curLexType = null;
        line = 0;
        beforeLine = 0;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        getNextLine();
    }

    public void nextToken() {
        if (curToken != "") {
            beforeLine = line;
        }
        try {
            curToken = getNextString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        switch (curToken) {
            case "main" :
                curLexType = LexType.MAINTK;
                break;
            case "const" :
                curLexType = LexType.CONSTTK;
                break;
            case "int" :
                curLexType = LexType.INTTK;
                break;
            case "break" :
                curLexType = LexType.BREAKTK;
                break;
            case "continue" :
                curLexType = LexType.CONTINUETK;
                break;
            case "if" :
                curLexType = LexType.IFTK;
                break;
            case "else" :
                curLexType = LexType.ELSETK;
                break;
            case "!" :
                curLexType = LexType.NOT;
                break;
            case "&&" :
                curLexType = LexType.AND;
                break;
            case "||" :
                curLexType = LexType.OR;
                break;
            case "for" :
                curLexType = LexType.FORTK;
                break;
            case "getint" :
                curLexType = LexType.GETINTTK;
                break;
            case "printf" :
                curLexType = LexType.PRINTFTK;
                break;
            case "return" :
                curLexType = LexType.RETURNTK;
                break;
            case "void" :
                curLexType = LexType.VOIDTK;
                break;
            case "+" :
                curLexType = LexType.PLUS;
                break;
            case "-" :
                curLexType = LexType.MINU;
                break;
            case "*" :
                curLexType = LexType.MULT;
                break;
            case "/" :
                curLexType = LexType.DIV;
                break;
            case "%" :
                curLexType = LexType.MOD;
                break;
            case "<" :
                curLexType = LexType.LSS;
                break;
            case "<=" :
                curLexType = LexType.LEQ;
                break;
            case ">" :
                curLexType = LexType.GRE;
                break;
            case ">=" :
                curLexType = LexType.GEQ;
                break;
            case "==" :
                curLexType = LexType.EQL;
                break;
            case "!=" :
                curLexType = LexType.NEQ;
                break;
            case "=" :
                curLexType = LexType.ASSIGN;
                break;
            case ";" :
                curLexType = LexType.SEMICN;
                break;
            case "," :
                curLexType = LexType.COMMA;
                break;
            case "(" :
                curLexType = LexType.LPARENT;
                break;
            case ")" :
                curLexType = LexType.RPARENT;
                break;
            case "[" :
                curLexType = LexType.LBRACK;
                break;
            case "]" :
                curLexType = LexType.RBRACK;
                break;
            case "{" :
                curLexType = LexType.LBRACE;
                break;
            case "}" :
                curLexType = LexType.RBRACE;
                break;
            case "" :
                curLexType = null;
                break;
            default : {
                if (curToken.charAt(0) == '"') {
                    curLexType = LexType.STRCON;
                }
                else if (Character.isDigit(curToken.charAt(0))) {
                    curLexType = LexType.INTCON;
                }
                else if (Character.isAlphabetic(curToken.charAt(0)) || curToken.charAt(0) == '_') {
                    curLexType = LexType.IDENFR;
                }
                else {
                    throw new RuntimeException();
                }
            }
        }
        if (!isEnd() && curLexType == null) {
            nextToken();
        }
    }

    public String getToken() {
        if (curToken == null) {
            try {
                throw new Exception("Token is null");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return curToken;
    }

    public LexType getLexType() {
        return curLexType;
    }

    public int getLine() {
        return line;
    }

    public void getNextLine() {
        if (!scanner.hasNextLine()) {
            return;
        }
        line++;
        source = scanner.nextLine();
        pos = 0;
        len = source.length();
    }

    public String getNextString() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isConstInt = false;
        while(true) {
            if (pos >= len) {
                if (stringBuilder.length() != 0 || isEnd()) {
                    break;
                }
                else {
                    getNextLine();
                }
            }
            else if ("+-*<>=;!&|{}[](),%".indexOf(source.charAt(pos)) >= 0) {
                if (stringBuilder.length() == 0 && pos + 1 < len &&
                        (source.charAt(pos) == '&' && source.charAt(pos + 1) == '&' ||
                        source.charAt(pos) == '|' && source.charAt(pos + 1) == '|' ||
                        "<>=!".indexOf(source.charAt(pos)) >= 0 && source.charAt(pos + 1) == '=')) {
                    stringBuilder.append(source.charAt(pos++));
                    stringBuilder.append(source.charAt(pos++));
                }
                else if (stringBuilder.length() == 0) {
                    stringBuilder.append(source.charAt(pos++));
                }
                break;
            }
            else if (source.charAt(pos) == '/') {
                if (pos + 1 < len && (source.charAt(pos + 1) == '*' || source.charAt(pos + 1) == '/')) {
                    passNote();
                }
                else if (stringBuilder.length() == 0) {
                    stringBuilder.append(source.charAt(pos++));
                }
                break;
            }
            else if (source.charAt(pos) == '_' || Character.isAlphabetic(source.charAt(pos)) ) {
                if (isConstInt) {
                    throw new Exception("wrong const int");
                }
                stringBuilder.append(source.charAt(pos++));
            }
            else if (Character.isDigit(source.charAt(pos))) {
                if (stringBuilder.length() == 0) {
                    isConstInt = true;
                }
                stringBuilder.append(source.charAt(pos++));
            }
            else if (source.charAt(pos) == ' ' || source.charAt(pos) == '\r'
                    || source.charAt(pos) == '\n' || source.charAt(pos) == '\t') {
                pos++;
                if (stringBuilder.length() != 0) {
                    break;
                }
            }
            else if (source.charAt(pos) == '"') {
                if (stringBuilder.length() != 0) {
                    break;
                }
                stringBuilder.append(source.charAt(pos++));
                while (source.charAt(pos) != '"') {
                    stringBuilder.append(source.charAt(pos++));
                }
                stringBuilder.append(source.charAt(pos++));
                break;
            }
            else {
                throw new Exception("wrong word " + source.charAt(pos));
            }
        }
        return stringBuilder.toString();
    }

    public void passNote() {
        pos++;
        assert pos < len;
        if (source.charAt(pos) == '/') {
            pos = len;
            if (scanner.hasNextLine()) {
                getNextLine();
            }
        }
        else if (source.charAt(pos) == '*') {
            while (true) {
                pos++;
                if (pos >= len) {
                    if (scanner.hasNextLine()) {
                        getNextLine();
                    }
                    else {
                        break;
                    }
                }
                if (pos + 1 < len && source.charAt(pos) == '*'
                        && source.charAt(pos + 1) == '/') {
                    pos += 2;
                    break;
                }
            }
        }
    }

    public boolean isEnd() {
        return !scanner.hasNext() && len <= pos;
    }

    public int getBeforeLine() {
        return beforeLine;
    }
}
