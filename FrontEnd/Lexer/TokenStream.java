package FrontEnd.Lexer;

import java.util.List;

public class TokenStream {
    private int watchPoint;

    private final List<Token> tokenList;

    public TokenStream(List<Token> tokenList) {
        this.tokenList = tokenList;
        watchPoint = 0;
    }

    public Token read() {
        return watchPoint < tokenList.size() ? tokenList.get(watchPoint++) : null;
    }

    public void unread() {
        watchPoint = Math.max(watchPoint - 1, 0);
    }

    public void unread(int n) {
        watchPoint = Math.max(watchPoint - n, 0);
    }

    public int getWatchPoint() {
        return watchPoint;
    }

    public void addToken(Token token) {
        tokenList.add(token);
    }
}
