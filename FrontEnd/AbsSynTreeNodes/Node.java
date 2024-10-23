package FrontEnd.AbsSynTreeNodes;

import Enums.SyntaxVarType;
import FrontEnd.AbsSynTreeNodes.Stmt.BreakStmt;
import FrontEnd.AbsSynTreeNodes.Stmt.ContinueStmt;
import FrontEnd.AbsSynTreeNodes.Stmt.Stmt;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Values.BasicBlock;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    protected ArrayList<Node> children;
    protected SyntaxVarType type;
    protected int startPos;
    protected int endPos;
    protected HashSet<SyntaxVarType> set;

    protected int startLine;

    protected int endLine;

    public Node(SyntaxVarType type, ArrayList<Node> children) {
        this.type = type;
        this.children = children;
        set = new HashSet<>();
        set.add(SyntaxVarType.TOKEN);
        set.add(SyntaxVarType.ILLEGAL);
        set.add(SyntaxVarType.BlockItem);
        set.add(SyntaxVarType.Decl);
        set.add(SyntaxVarType.BType);
        set.add(SyntaxVarType.IntConst);
        if (children.size() == 0) {
            this.startPos = this.endPos = -1;
            return;
        }
        this.startPos = children.get(0).startPos;
        this.endPos = children.get(children.size() - 1).endPos;
        if (type != SyntaxVarType.TOKEN) {
            this.startLine = children.get(0).startLine;
            this.endLine = children.get(children.size() - 1).endLine;
        }
    }

    public int getSize() {
        if (endPos == -1) return 0;
        return endPos - startPos + 1;
    }

    public SyntaxVarType getType() {
        return type;
    }

    public void setType(SyntaxVarType type) {
        this.type = type;
    }

    public void print(PrintStream printWriter) {
        for (Node node : children) {
            node.print(printWriter);
        }
        if (!set.contains(type))
            printWriter.println("<" + type.toString() + ">");
        else if (type == SyntaxVarType.TOKEN) printWriter.println(children.get(0).toString());
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void checkError() {
        for (Node child : children) child.checkError();
    }

    public int getDim() {
        return -1;
    }

    public LLVM_Value genLLVMir() {
        for (Node child : children) {
            child.genLLVMir();
        }
        return null;
    }

    public void setBlockForLoop(BasicBlock Stmt2Block, BasicBlock NextBlock) {
        if (this instanceof BreakStmt || this instanceof ContinueStmt) ((Stmt) this).setBlock(Stmt2Block, NextBlock);
        for (Node child : children) {
            if (child instanceof BreakStmt || child instanceof ContinueStmt) {
                ((Stmt) child).setBlock(Stmt2Block, NextBlock);
            } else {
                child.setBlockForLoop(Stmt2Block, NextBlock);
            }
        }
    }
}
