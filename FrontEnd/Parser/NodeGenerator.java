package FrontEnd.Parser;


import Enums.SyntaxVarType;
import FrontEnd.Lexer.Token;
import FrontEnd.AbsSynTreeNodes.*;
import FrontEnd.AbsSynTreeNodes.Stmt.*;
import FrontEnd.AbsSynTreeNodes.Var.*;
import FrontEnd.AbsSynTreeNodes.Exp.*;
import FrontEnd.AbsSynTreeNodes.Func.*;
import FrontEnd.AbsSynTreeNodes.Var.Number;

import java.util.ArrayList;

public class NodeGenerator {
    public static Node generateNode(SyntaxVarType type, ArrayList<Node> children) {
        return switch (type) {
            case Block -> new Block(type, children);
            case VarDecl -> new VarDecl(type, children);
            case ConstDecl -> new ConstDecl(type, children);
            case FuncDef -> new FuncDef(type, children);
            case MainFuncDef -> new MainFuncDef(type, children);
            case FuncFParams -> new FuncFParams(type, children);
            case FuncFParam -> new FuncFParam(type, children);
            case FuncRParams -> new FuncRParams(type, children);
            case FuncType -> new FuncType(type, children);
            case VarDef -> new VarDef(type, children);
            case ConstDef -> new ConstDef(type, children);
            case ConstInitVal -> new ConstInitVal(type, children);
            case Stmt -> new Stmt(type, children);
            case LVal -> new LVal(type, children);
            case Cond -> new Cond(type, children);
            case ForStmt -> new ForStmt(type, children);
            case AddExp -> new AddExp(type, children);
            case LOrExp -> new LOrExp(type, children);
            case PrimaryExp -> new PrimaryExp(type, children);
            case Number -> new Number(type, children);
            case IntConst -> new IntConst(type, children);
            case UnaryExp -> new UnaryExp(type, children);
            case UnaryOp -> new UnaryOp(type, children);
            case MulExp -> new MulExp(type, children);
            case RelExp -> new RelExp(type, children);
            case EqExp -> new EqExp(type, children);
            case LAndExp -> new LAndExp(type, children);
            case CompUnit -> new CompUnit(type, children);
            case Decl -> new Decl(type, children);
            case BlockItem -> new BlockItem(type, children);
            case Exp -> new Exp(type, children);
            case BType -> new BType(type, children);
            case InitVal -> new InitVal(type, children);
            case ConstExp -> new ConstExp(type, children);
            case ILLEGAL -> new Node(type, children);
            case AssignStmt -> new AssignStmt(type, children);
            case ExpStmt -> new ExpStmt(type, children);
            case IfStmt -> new IfStmt(type, children);
            case ForLoopStmt -> new ForLoopStmt(type, children);
            case BreakStmt -> new BreakStmt(type, children);
            case ContinueStmt -> new ContinueStmt(type, children);
            case ReturnStmt -> new ReturnStmt(type, children);
            case PrintfStmt -> new PrintfStmt(type, children);
            case BlockStmt -> new BlockStmt(type, children);
            case GetIntStmt -> new GetIntStmt(type, children);
            default -> null;
        };
    }

    public static TokenNode generateToken(Token token, int pos) {
        return new TokenNode(SyntaxVarType.TOKEN, token, pos);
    }
}
