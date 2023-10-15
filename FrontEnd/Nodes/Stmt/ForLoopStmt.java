package FrontEnd.Nodes.Stmt;

import Enums.SyntaxVarType;
import FrontEnd.Nodes.Node;
import FrontEnd.Symbol.SymbolManager;

import java.util.ArrayList;

public class ForLoopStmt extends Stmt {
    public ForLoopStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().enterLoopBlock();
        super.checkError();
        SymbolManager.getInstance().leaveLoopBlock();
    }
}
