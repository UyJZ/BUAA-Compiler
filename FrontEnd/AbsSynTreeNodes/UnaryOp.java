package FrontEnd.AbsSynTreeNodes;

import FrontEnd.Lexer.Token;

import java.util.ArrayList;

public class UnaryOp extends SynTreeNode {
    public UnaryOp(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
    }

    public Token.TokenType getOp() {
        return ((TokenSynTreeNode) children.get(0)).getTokenType();
    }
}
