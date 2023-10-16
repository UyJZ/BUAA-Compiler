package FrontEnd.Nodes;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Lexer.Token;
import FrontEnd.Symbol.SymbolManager;

import java.io.PrintStream;
import java.util.ArrayList;

public class TokenNode extends Node {

    private final Token token;

    public TokenNode(SyntaxVarType type, Token token, int pos) {
        super(type, new ArrayList<>());
        this.token = token;
        super.startPos = super.endPos = pos;
        super.type = type;
        super.startLine = super.endLine = token.getLine();
    }

    @Override
    public String toString() {
        return token.toString();
    }

    public String getIdentName() {
        if (token.getType() != tokenType.IDENFR) return null;
        else return token.getValue();
    }

    public boolean isFormatString() {
        return token.getType() == tokenType.STRCON;
    }

    public tokenType getTokenType() {
        return token.getType();
    }

    @Override
    public void print(PrintStream printWriter) {
        printWriter.println(token.toString());
    }

    @Override
    public int getStartLine() {
        return token.getLine();
    }

    @Override
    public int getEndLine() {
        return token.getLine();
    }

    private void checkFormatStringError() {
        //TODO: check format string error
        StringBuilder formatString = new StringBuilder(token.getValue().substring(1, token.getValue().length() - 1));
        if (!parseFormatString(formatString))
            ErrorChecker.AddError(new Error(token.getLine(), ErrorType.a));
    }

    public int getParameterNum() {
        if (token.getType() != tokenType.STRCON) return -1;
        else return token.toString().split("%d").length - 1;
    }

    public String getValue() {
        return token.getValue();
    }

    private boolean parseFormatString(StringBuilder formatString) {
        int curPos = 0;
        String space = "\t\0 ";
        while (curPos < formatString.length()) {
            while (space.contains(formatString.substring(curPos, curPos + 1))) {
                curPos++;
            }
            if (curPos >= formatString.length()) break;
            char lookAhead = formatString.charAt(curPos), lookAhead2 = (curPos + 1 < formatString.length()) ? formatString.charAt(curPos + 1) : '\0';
            boolean inRange = (int) lookAhead == 32 || (int) lookAhead == 33 ||
                    ((int) lookAhead >= 40 && (int) lookAhead <= 126 && (int) lookAhead != 92);
            if (curPos + 1 < formatString.length() && lookAhead == '%' && lookAhead2 == 'd' || lookAhead == '\\' && lookAhead2 == 'n')
                curPos++;
            else if (!inRange) return false;
            curPos++;
        }
        return true;
    }

    @Override
    public void checkError() {
        switch (token.getType()) {
            case STRCON -> checkFormatStringError();
            case IDENFR -> //TODO: check ident error:1.rename error 2.undeclared error
                //step1: check if symbolTable has a symbol with the same name
                //step2: check if the symbol is a function
                //step3: check if the symbol is a variable
                //step4: check if the symbol is a constant
                //step5: push it into symbolTable
                    System.out.println("check ident error:1.rename error 2.undeclared error");
        }
    }

    public int getDim() {
        switch (token.getType()) {
            case IDENFR -> {
                return SymbolManager.getInstance().getDimByName(getIdentName());
            }
            case INTCON -> {
                return 0;
            }
            default -> {
                return -1;
            }
        }
    }
}
