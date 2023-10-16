package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;

import java.util.ArrayList;

public class PrintfStmt extends Stmt {

    private TokenNode formatStringNode;

    private int OutputNums = 0;

    public PrintfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node n : children)
            if (n instanceof TokenNode && ((TokenNode) n).isFormatString())
                formatStringNode = (TokenNode) n;
            else if (n instanceof Exp) OutputNums++;
    }

    @Override
    public void checkError() {
        if (OutputNums != formatStringNode.getParameterNum())
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.l));
        super.checkError();
    }
}
