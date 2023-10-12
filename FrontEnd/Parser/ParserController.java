package FrontEnd.Parser;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Lexer.Token;
import FrontEnd.Lexer.TokenStream;
import FrontEnd.Nodes.Node;

import java.util.ArrayList;

public class ParserController {

    private final TokenStream tokenStream;


    private Token curToken;

    public ParserController(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    private void read() {
        curToken = tokenStream.read();
    }

    private void unread() {
        tokenStream.unread();
    }

    private void unread(int n) {
        tokenStream.unread(n);
    }

    public Node parse() {
        return Parse_CompUnit();
    }

    public Node Parse_CompUnit() {
        ArrayList<Node> children = new ArrayList<>();
        while (true) {
            Node decl = Parse_Decl();
            if (decl.getType() != SyntaxVarType.ILLEGAL) children.add(decl);
            else {
                unread(decl.getSize());
                break;
            }
        }
        while (true) {
            Node funcDef = Parse_FuncDef();
            if (funcDef.getType() != SyntaxVarType.ILLEGAL) children.add(funcDef);
            else {
                unread(funcDef.getSize());
                break;
            }
        }
        Node mainFuncDef = Parse_MainFuncDef();
        if (mainFuncDef.getType() != SyntaxVarType.ILLEGAL) children.add(mainFuncDef);
        else {
            unread(mainFuncDef.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.CompUnit, children);
    }

    public Node Parse_Decl() {
        ArrayList<Node> children = new ArrayList<>();
        Node constDecl = Parse_ConstDecl();
        if (constDecl.getType() != SyntaxVarType.ILLEGAL) {
            children.add(constDecl);
            return NodeGenerator.generateNode(SyntaxVarType.Decl, children);
        } else unread(constDecl.getSize());
        Node varDecl = Parse_VarDecl();
        if (varDecl.getType() != SyntaxVarType.ILLEGAL) {
            children.add(varDecl);
            return NodeGenerator.generateNode(SyntaxVarType.Decl, children);
        } else unread(varDecl.getSize());
        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
    }

    public Node Parse_FuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node funcType = Parse_FuncType();
        if (funcType.getType() != SyntaxVarType.ILLEGAL) children.add(funcType);
        else {
            unread(funcType.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        Node funcFParams = Parse_FuncFParams();
        if (funcFParams.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParams);
        else unread(funcFParams.getSize());
        read();
        if (curToken.getType() != tokenType.RPARENT) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.FuncDef, children);
    }

    public Node Parse_MainFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTTK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.MAINTK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.RPARENT) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.MainFuncDef, children);
    }

    public Node Parse_ConstDecl() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.CONSTTK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node constDef = Parse_ConstDef();
        if (constDef.getType() != SyntaxVarType.ILLEGAL) children.add(constDef);
        else {
            unread(constDef.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            constDef = Parse_ConstDef();
            if (constDef.getType() != SyntaxVarType.ILLEGAL) children.add(constDef);
            else {
                unread(constDef.getSize());
                break;
            }
        }
        read();
        if (curToken.getType() != tokenType.SEMICN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SyntaxVarType.ConstDecl, children);
        }
    }

    public Node Parse_VarDecl() {
        ArrayList<Node> children = new ArrayList<>();
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node varDef = Parse_VarDef();
        if (varDef.getType() != SyntaxVarType.ILLEGAL) children.add(varDef);
        else {
            unread(varDef.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            ArrayList<Node> list = new ArrayList<>();
            read();
            //parse ,
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else list.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            Node varDef1 = Parse_VarDef();
            if (varDef1.getType() != SyntaxVarType.ILLEGAL) list.add(varDef1);
            else {
                unread(varDef1.getSize());
                break;
            }
            children.addAll(list);
        }
        read();
        if (curToken.getType() != tokenType.SEMICN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SyntaxVarType.VarDecl, children);
        }
    }

    public Node Parse_BType() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTTK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SyntaxVarType.BType, children);
    }

    public Node Parse_ConstDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ConstDef, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        Node constInitVal = Parse_ConstInitVal();
        if (constInitVal.getType() != SyntaxVarType.ILLEGAL) children.add(constInitVal);
        else {
            unread(constInitVal.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.ConstDef, children);
    }

    public Node Parse_Ident() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.IDENFR) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SyntaxVarType.Ident, children);
    }

    public Node Parse_ConstExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.ConstExp, children);
    }

    public Node Parse_ConstInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node constExp = Parse_ConstExp();
        if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
        else {
            unread(constExp.getSize());
            read();
            if (curToken.getType() != tokenType.LBRACE) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            Node constInitVal = Parse_ConstInitVal();
            if (constInitVal.getType() != SyntaxVarType.ILLEGAL) {
                children.add(constInitVal);
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    constInitVal = Parse_ConstInitVal();
                    if (constInitVal.getType() != SyntaxVarType.ILLEGAL) children.add(constInitVal);
                    else {
                        unread(constInitVal.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RBRACE) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SyntaxVarType.ConstInitVal, children);
            } else {
                unread(constInitVal.getSize());
                read();
                if (curToken.getType() == tokenType.RBRACE) {
                    children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SyntaxVarType.ConstInitVal, children);
                }
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.ConstInitVal, children);
    }

    public Node Parse_VarDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.VarDef, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        Node initVal = Parse_InitVal();
        if (initVal.getType() != SyntaxVarType.ILLEGAL) children.add(initVal);
        else {
            unread(initVal.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.VarDef, children);
    }

    public Node Parse_InitVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node exp = Parse_Exp();
        if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            read();
            if (curToken.getType() != tokenType.LBRACE) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node initVal = Parse_InitVal();
            if (initVal.getType() != SyntaxVarType.ILLEGAL) {
                children.add(initVal);
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    initVal = Parse_InitVal();
                    if (initVal.getType() != SyntaxVarType.ILLEGAL) children.add(initVal);
                    else {
                        unread(initVal.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RBRACE) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SyntaxVarType.InitVal, children);
            } else {
                unread(initVal.getSize());
                read();
                if (curToken.getType() == tokenType.RBRACE) {
                    children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SyntaxVarType.InitVal, children);
                }
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.InitVal, children);
    }

    public Node Parse_Exp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.Exp, children);
    }

    public Node Parse_FuncType() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() == tokenType.VOIDTK || curToken.getType() == tokenType.INTTK)
            children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        else {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.FuncType, children);
    }

    public Node Parse_FuncFParams() {
        ArrayList<Node> children = new ArrayList<>();
        Node funcFParam = Parse_FuncFParam();
        if (funcFParam.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParam);
        else {
            unread(funcFParam.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            funcFParam = Parse_FuncFParam();
            if (funcFParam.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParam);
            else {
                unread(funcFParam.getSize());
                break;
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.FuncFParams, children);
    }

    public Node Parse_Block() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.LBRACE) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        while (true) {
            Node blockItem = Parse_BlockItem();
            if (blockItem.getType() != SyntaxVarType.ILLEGAL) children.add(blockItem);
            else {
                unread(blockItem.getSize());
                break;
            }
        }
        read();
        if (curToken.getType() != tokenType.RBRACE) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SyntaxVarType.Block, children);
    }

    public Node Parse_FuncFParam() {
        ArrayList<Node> children = new ArrayList<>();
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.LBRACK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.FuncFParam, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.RBRACK) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        }
        return NodeGenerator.generateNode(SyntaxVarType.FuncFParam, children);
    }

    public Node Parse_BlockItem() {
        ArrayList<Node> children = new ArrayList<>();
        Node decl = Parse_Decl();
        if (decl.getType() != SyntaxVarType.ILLEGAL) children.add(decl);
        else {
            unread(decl.getSize());
            Node stmt = Parse_Stmt();
            if (stmt.getType() != SyntaxVarType.ILLEGAL) children.add(stmt);
            else {
                unread(stmt.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.BlockItem, children);
    }

    public Node Parse_Stmt() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case SEMICN -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
            }
            case IFTK -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node cond = Parse_Cond();
                if (cond.getType() != SyntaxVarType.ILLEGAL) children.add(cond);
                else {
                    unread(cond.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node stmt1 = Parse_Stmt();
                if (stmt1.getType() != SyntaxVarType.ILLEGAL) children.add(stmt1);
                else {
                    unread(stmt1.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != tokenType.ELSETK) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node stmt2 = Parse_Stmt();
                if (stmt2.getType() != SyntaxVarType.ILLEGAL) children.add(stmt2);
                else {
                    unread(stmt2.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
                return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
            }
            case FORTK -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node forStmt1 = Parse_ForStmt();
                if (forStmt1.getType() != SyntaxVarType.ILLEGAL) children.add(forStmt1);
                else unread(forStmt1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node cond1 = Parse_Cond();
                if (cond1.getType() != SyntaxVarType.ILLEGAL) children.add(cond1);
                else unread(cond1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node forStmt2 = Parse_ForStmt();
                if (forStmt2.getType() != SyntaxVarType.ILLEGAL) children.add(forStmt2);
                else unread(forStmt2.getSize());
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node stmt3 = Parse_Stmt();
                if (stmt3.getType() != SyntaxVarType.ILLEGAL) children.add(stmt3);
                else {
                    unread(stmt3.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
                return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
            }
            case BREAKTK, CONTINUETK -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
                }
            }
            case RETURNTK -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node exp1 = Parse_Exp();
                if (exp1.getType() != SyntaxVarType.ILLEGAL) children.add(exp1);
                else unread(exp1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
                }
            }
            case PRINTFTK -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                Node formatString = Parse_FormatString();
                if (formatString.getType() != SyntaxVarType.ILLEGAL) children.add(formatString);
                else {
                    unread(formatString.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else
                        children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    Node exp2 = Parse_Exp();
                    if (exp2.getType() != SyntaxVarType.ILLEGAL) children.add(exp2);
                    else {
                        unread(exp2.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
                }
            }
            default -> unread();
        }
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            read();
            if (curToken.getType() != tokenType.ASSIGN) {
                unread();
                unread(NodeGenerator.generateNode(SyntaxVarType.Stmt, children).getSize());
                children.clear();
            } else {
                children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.GETINTTK) {
                    unread();
                    Node exp3 = Parse_Exp();
                    if (exp3.getType() != SyntaxVarType.ILLEGAL) children.add(exp3);
                    else {
                        unread(exp3.getSize());
                        unread(NodeGenerator.generateNode(SyntaxVarType.Stmt, children).getSize());
                        children.clear();
                    }
                } else {
                    children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.LPARENT) {
                        unread();
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                    children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.RPARENT) {
                        unread();
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                    children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.SEMICN) {
                        unread();
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    } else {
                        children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                        return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
                    }
                }
            }
        }
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) {
            children.add(block);
            return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
        } else unread(block.getSize());
        Node exp5 = Parse_Exp();
        if (exp5.getType() != SyntaxVarType.ILLEGAL) children.add(exp5);
        else unread(exp5.getSize());
        read();
        if (curToken.getType() != tokenType.SEMICN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SyntaxVarType.Stmt, children);
        }
    }

    public Node Parse_LVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node exp = Parse_Exp();
            if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        }
        return NodeGenerator.generateNode(SyntaxVarType.LVal, children);
    }

    public Node Parse_Cond() {
        ArrayList<Node> children = new ArrayList<>();
        Node lOrExp = Parse_LOrExp();
        if (lOrExp.getType() != SyntaxVarType.ILLEGAL) children.add(lOrExp);
        else {
            unread(lOrExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.Cond, children);
    }

    public Node Parse_ForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) children.add(lVal);
        else {
            unread(lVal.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        Node exp = Parse_Exp();
        if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.ForStmt, children);
    }

    public Node Parse_FormatString() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.STRCON) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SyntaxVarType.FormatString, children);
    }

    public Node Parse_AddExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node mulExp = Parse_MulExp();
        if (mulExp.getType() != SyntaxVarType.ILLEGAL) children.add(mulExp);
        else {
            unread(mulExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before = NodeGenerator.generateNode(SyntaxVarType.AddExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case PLUS, MINU -> {
                    before = NodeGenerator.generateNode(SyntaxVarType.AddExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    Node mulExp1 = Parse_MulExp();
                    if (mulExp1.getType() != SyntaxVarType.ILLEGAL) children.add(mulExp1);
                    else {
                        unread(mulExp1.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.AddExp, children);
                }
            }
        }
    }

    public Node Parse_LOrExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node landExp = Parse_LAndExp();
        if (landExp.getType() != SyntaxVarType.ILLEGAL) children.add(landExp);
        else {
            unread(landExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before = NodeGenerator.generateNode(SyntaxVarType.LOrExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() != tokenType.OR) {
                unread();
                break;
            } else {
                before = NodeGenerator.generateNode(SyntaxVarType.LOrExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            }
            Node lAndExp = Parse_LAndExp();
            if (lAndExp.getType() != SyntaxVarType.ILLEGAL) children.add(lAndExp);
            else {
                unread(lAndExp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.LOrExp, children);
    }

    public Node Parse_PrimaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
        } else {
            children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node exp = Parse_Exp();
            if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RPARENT) {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SyntaxVarType.PrimaryExp, children);
        }
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            return NodeGenerator.generateNode(SyntaxVarType.PrimaryExp, children);
        } else {
            unread(lVal.getSize());
        }
        Node number = Parse_Number();
        if (number.getType() != SyntaxVarType.ILLEGAL) {
            children.add(number);
            return NodeGenerator.generateNode(SyntaxVarType.PrimaryExp, children);
        } else {
            unread(number.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
    }

    public Node Parse_Number() {
        ArrayList<Node> children = new ArrayList<>();
        Node intConst = Parse_IntConst();
        if (intConst.getType() != SyntaxVarType.ILLEGAL) children.add(intConst);
        else {
            unread(intConst.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SyntaxVarType.Number, children);
    }

    public Node Parse_IntConst() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTCON) {
            unread();
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SyntaxVarType.IntConst, children);
    }

    public Node Parse_UnaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node indent = Parse_Ident();
        if (indent.getType() != SyntaxVarType.ILLEGAL) {
            children.add(indent);
            read();
            if (curToken.getType() != tokenType.LPARENT) {
                unread();
                unread(NodeGenerator.generateNode(SyntaxVarType.UnaryExp, children).getSize());
                children.clear();
            } else {
                children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                Node funcRParams = Parse_FuncRParams();
                if (funcRParams.getType() != SyntaxVarType.ILLEGAL) children.add(funcRParams);
                else unread(funcRParams.getSize());
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SyntaxVarType.UnaryExp, children);
            }
        } else unread(indent.getSize());
        Node unaryOp = Parse_UnaryOp();
        if (unaryOp.getType() != SyntaxVarType.ILLEGAL) {
            children.add(unaryOp);
            Node unaryExp = Parse_UnaryExp();
            if (unaryExp.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp);
            else {
                unread(unaryExp.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
            return NodeGenerator.generateNode(SyntaxVarType.UnaryExp, children);
        } else {
            unread(unaryOp.getSize());
        }
        Node primaryExp = Parse_PrimaryExp();
        if (primaryExp.getType() != SyntaxVarType.ILLEGAL) {
            children.add(primaryExp);
            return NodeGenerator.generateNode(SyntaxVarType.UnaryExp, children);
        } else unread(primaryExp.getSize());
        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
    }

    public Node Parse_UnaryOp() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case PLUS, MINU, NOT -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SyntaxVarType.UnaryOp, children);
            }
            default -> {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
    }

    public Node Parse_MulExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node unaryExp = Parse_UnaryExp();
        if (unaryExp.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp);
        else {
            unread(unaryExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before;
        while (true) {
            read();
            switch (curToken.getType()) {
                case MULT, DIV, MOD -> {
                    before = NodeGenerator.generateNode(SyntaxVarType.MulExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    Node unaryExp1 = Parse_UnaryExp();
                    if (unaryExp1.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp1);
                    else {
                        unread(unaryExp1.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.MulExp, children);
                }
            }
        }
    }

    public Node Parse_RelExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before = NodeGenerator.generateNode(SyntaxVarType.RelExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case GEQ, GRE, LEQ, LSS -> {
                    before = NodeGenerator.generateNode(SyntaxVarType.RelExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    Node addExp1 = Parse_AddExp();
                    if (addExp1.getType() != SyntaxVarType.ILLEGAL) children.add(addExp1);
                    else {
                        unread(addExp1.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.RelExp, children);
                }
            }
        }
    }

    public Node Parse_EqExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node relExp = Parse_RelExp();
        if (relExp.getType() != SyntaxVarType.ILLEGAL) children.add(relExp);
        else {
            unread(relExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before = NodeGenerator.generateNode(SyntaxVarType.EqExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case EQL, NEQ -> {
                    before = NodeGenerator.generateNode(SyntaxVarType.EqExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    Node relExp1 = Parse_RelExp();
                    if (relExp1.getType() != SyntaxVarType.ILLEGAL) children.add(relExp1);
                    else {
                        unread(relExp1.getSize());
                        return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SyntaxVarType.EqExp, children);
                }
            }
        }
    }

    public Node Parse_LAndExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node eqExp = Parse_EqExp();
        if (eqExp.getType() != SyntaxVarType.ILLEGAL) children.add(eqExp);
        else {
            unread(eqExp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        Node before = NodeGenerator.generateNode(SyntaxVarType.LAndExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() == tokenType.AND) {
                before = NodeGenerator.generateNode(SyntaxVarType.LAndExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
                Node eqExp1 = Parse_EqExp();
                if (eqExp1.getType() != SyntaxVarType.ILLEGAL) children.add(eqExp1);
                else {
                    unread(eqExp1.getSize());
                    return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
                }
            } else {
                unread();
                return NodeGenerator.generateNode(SyntaxVarType.LAndExp, children);
            }
        }
    }

    public Node Parse_FuncRParams() {
        ArrayList<Node> children = new ArrayList<>();
        Node exp = Parse_Exp();
        if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken( curToken, tokenStream.getWatchPoint()));
            Node exp1 = Parse_Exp();
            if (exp1.getType() != SyntaxVarType.ILLEGAL) children.add(exp1);
            else {
                unread(exp1.getSize());
                return NodeGenerator.generateNode(SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SyntaxVarType.FuncRParams, children);
    }

}

