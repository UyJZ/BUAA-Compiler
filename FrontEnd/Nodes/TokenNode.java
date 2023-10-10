package FrontEnd.Nodes;

import Enums.SyntaxVarType;
import FrontEnd.Lexer.Token;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TokenNode extends Node {

    private final Token token;

    public TokenNode(SyntaxVarType type, Token token, int pos) {
        super(type, new ArrayList<>());
        this.token = token;
        super.startPos = super.endPos = pos;
        super.type = type;
    }

    @Override
    public String toString() {
        return token.toString();
    }

    @Override
    public void print(PrintStream printWriter) {
        printWriter.println(token.toString());
    }
}
