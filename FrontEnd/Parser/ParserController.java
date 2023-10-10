package FrontEnd.Parser;

import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.Lexer.Token;
import FrontEnd.Lexer.TokenStream;
import FrontEnd.Nodes.CompUnit;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.WordList;

import java.util.ArrayList;
import java.util.Collection;

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
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new CompUnit(SyntaxVarType.CompUnit, children);
    }

    public Node Parse_Decl() {
        ArrayList<Node> children = new ArrayList<>();
        Node constDecl = Parse_ConstDecl();
        if (constDecl.getType() != SyntaxVarType.ILLEGAL) {
            children.add(constDecl);
            return new Node(SyntaxVarType.Decl, children);
        } else unread(constDecl.getSize());
        Node varDecl = Parse_VarDecl();
        if (varDecl.getType() != SyntaxVarType.ILLEGAL) {
            children.add(varDecl);
            return new Node(SyntaxVarType.Decl, children);
        } else unread(varDecl.getSize());
        return new Node(SyntaxVarType.ILLEGAL, children);
    }

    public Node Parse_FuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node funcType = Parse_FuncType();
        if (funcType.getType() != SyntaxVarType.ILLEGAL) children.add(funcType);
        else {
            unread(funcType.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node funcFParams = Parse_FuncFParams();
        if (funcFParams.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParams);
        else unread(funcFParams.getSize());
        read();
        if (curToken.getType() != tokenType.RPARENT) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.FuncDef, children);
    }

    public Node Parse_MainFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTTK) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.MAINTK) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.RPARENT) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.MainFuncDef, children);
    }

    public Node Parse_ConstDecl() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.CONSTTK) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node constDef = Parse_ConstDef();
        if (constDef.getType() != SyntaxVarType.ILLEGAL) children.add(constDef);
        else {
            unread(constDef.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
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
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            return new Node(SyntaxVarType.ConstDecl, children);
        }
    }

    public Node Parse_VarDecl() {
        ArrayList<Node> children = new ArrayList<>();
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node varDef = Parse_VarDef();
        if (varDef.getType() != SyntaxVarType.ILLEGAL) children.add(varDef);
        else {
            unread(varDef.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            ArrayList<Node> list = new ArrayList<>();
            read();
            //parse ,
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else list.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
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
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            return new Node(SyntaxVarType.VarDecl, children);
        }
    }

    public Node Parse_BType() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTTK) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        return new Node(SyntaxVarType.BType, children);
    }

    public Node Parse_ConstDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node constInitVal = Parse_ConstInitVal();
        if (constInitVal.getType() != SyntaxVarType.ILLEGAL) children.add(constInitVal);
        else {
            unread(constInitVal.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.ConstDef, children);
    }

    public Node Parse_Ident() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.IDENFR) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        return new Node(SyntaxVarType.Ident, children);
    }

    public Node Parse_ConstExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.ConstExp, children);
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
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node constInitVal = Parse_ConstInitVal();
            if (constInitVal.getType() != SyntaxVarType.ILLEGAL) {
                children.add(constInitVal);
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    constInitVal = Parse_ConstInitVal();
                    if (constInitVal.getType() != SyntaxVarType.ILLEGAL) children.add(constInitVal);
                    else {
                        unread(constInitVal.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RBRACE) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                return new Node(SyntaxVarType.ConstInitVal, children);
            } else {
                unread(constInitVal.getSize());
                read();
                if (curToken.getType() == tokenType.RBRACE) {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    return new Node(SyntaxVarType.ConstInitVal, children);
                }
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
        }
        return new Node(SyntaxVarType.ConstInitVal, children);
    }

    public Node Parse_VarDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return new Node(SyntaxVarType.VarDef, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node initVal = Parse_InitVal();
        if (initVal.getType() != SyntaxVarType.ILLEGAL) children.add(initVal);
        else {
            unread(initVal.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.VarDef, children);
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
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node initVal = Parse_InitVal();
            if (initVal.getType() != SyntaxVarType.ILLEGAL) {
                children.add(initVal);
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    initVal = Parse_InitVal();
                    if (initVal.getType() != SyntaxVarType.ILLEGAL) children.add(initVal);
                    else {
                        unread(initVal.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RBRACE) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                return new Node(SyntaxVarType.InitVal, children);
            } else {
                unread(initVal.getSize());
                read();
                if (curToken.getType() == tokenType.RBRACE) {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    return new Node(SyntaxVarType.InitVal, children);
                }
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
        }
        return new Node(SyntaxVarType.InitVal, children);
    }

    public Node Parse_Exp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.Exp, children);
    }

    public Node Parse_FuncType() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() == tokenType.VOIDTK || curToken.getType() == tokenType.INTTK)
            children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        else {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.FuncType, children);
    }

    public Node Parse_FuncFParams() {
        ArrayList<Node> children = new ArrayList<>();
        Node funcFParam = Parse_FuncFParam();
        if (funcFParam.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParam);
        else {
            unread(funcFParam.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            funcFParam = Parse_FuncFParam();
            if (funcFParam.getType() != SyntaxVarType.ILLEGAL) children.add(funcFParam);
            else {
                unread(funcFParam.getSize());
                break;
            }
        }
        return new Node(SyntaxVarType.FuncFParams, children);
    }

    public Node Parse_Block() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.LBRACE) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
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
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        return new Node(SyntaxVarType.Block, children);
    }

    public Node Parse_FuncFParam() {
        ArrayList<Node> children = new ArrayList<>();
        Node bType = Parse_BType();
        if (bType.getType() != SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.LBRACK) {
            unread();
            return new Node(SyntaxVarType.FuncFParam, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != tokenType.RBRACK) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node constExp = Parse_ConstExp();
            if (constExp.getType() != SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        }
        return new Node(SyntaxVarType.FuncFParam, children);
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
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
        }
        return new Node(SyntaxVarType.BlockItem, children);
    }

    public Node Parse_Stmt() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case SEMICN:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                return new Node(SyntaxVarType.Stmt, children);
            case IFTK:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node cond = Parse_Cond();
                if (cond.getType() != SyntaxVarType.ILLEGAL) children.add(cond);
                else {
                    unread(cond.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node stmt1 = Parse_Stmt();
                if (stmt1.getType() != SyntaxVarType.ILLEGAL) children.add(stmt1);
                else {
                    unread(stmt1.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != tokenType.ELSETK) {
                    unread();
                    return new Node(SyntaxVarType.Stmt, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node stmt2 = Parse_Stmt();
                if (stmt2.getType() != SyntaxVarType.ILLEGAL) children.add(stmt2);
                else {
                    unread(stmt2.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
                return new Node(SyntaxVarType.Stmt, children);
            case FORTK:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node forStmt1 = Parse_ForStmt();
                if (forStmt1.getType() != SyntaxVarType.ILLEGAL) children.add(forStmt1);
                else unread(forStmt1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node cond1 = Parse_Cond();
                if (cond1.getType() != SyntaxVarType.ILLEGAL) children.add(cond1);
                else unread(cond1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node forStmt2 = Parse_ForStmt();
                if (forStmt2.getType() != SyntaxVarType.ILLEGAL) children.add(forStmt2);
                else unread(forStmt2.getSize());
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node stmt3 = Parse_Stmt();
                if (stmt3.getType() != SyntaxVarType.ILLEGAL) children.add(stmt3);
                else {
                    unread(stmt3.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
                return new Node(SyntaxVarType.Stmt, children);
            case BREAKTK:
            case CONTINUETK:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    return new Node(SyntaxVarType.Stmt, children);
                }
            case RETURNTK:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node exp1 = Parse_Exp();
                if (exp1.getType() != SyntaxVarType.ILLEGAL) children.add(exp1);
                else unread(exp1.getSize());
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    return new Node(SyntaxVarType.Stmt, children);
                }
            case PRINTFTK:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.LPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node formatString = Parse_FormatString();
                if (formatString.getType() != SyntaxVarType.ILLEGAL) children.add(formatString);
                else {
                    unread(formatString.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
                while (true) {
                    read();
                    if (curToken.getType() != tokenType.COMMA) {
                        unread();
                        break;
                    } else
                        children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    Node exp2 = Parse_Exp();
                    if (exp2.getType() != SyntaxVarType.ILLEGAL) children.add(exp2);
                    else {
                        unread(exp2.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.SEMICN) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    return new Node(SyntaxVarType.Stmt, children);
                }
            default:
                unread();
        }
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            read();
            if (curToken.getType() != tokenType.ASSIGN) {
                unread();
                unread(new Node(SyntaxVarType.Stmt, children).getSize());
                children.clear();
            } else {
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != tokenType.GETINTTK) {
                    unread();
                    Node exp3 = Parse_Exp();
                    if (exp3.getType() != SyntaxVarType.ILLEGAL) children.add(exp3);
                    else {
                        unread(exp3.getSize());
                        unread(new Node(SyntaxVarType.Stmt, children).getSize());
                        children.clear();
                    }
                } else {
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.LPARENT) {
                        unread();
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.RPARENT) {
                        unread();
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != tokenType.SEMICN) {
                        unread();
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    } else {
                        children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                        return new Node(SyntaxVarType.Stmt, children);
                    }
                }
            }
        }
        Node block = Parse_Block();
        if (block.getType() != SyntaxVarType.ILLEGAL) {
            children.add(block);
            return new Node(SyntaxVarType.Stmt, children);
        } else unread(block.getSize());
        Node exp5 = Parse_Exp();
        if (exp5.getType() != SyntaxVarType.ILLEGAL) children.add(exp5);
        else unread(exp5.getSize());
        read();
        if (curToken.getType() != tokenType.SEMICN) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            return new Node(SyntaxVarType.Stmt, children);
        }
    }

    public Node Parse_LVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node ident = Parse_Ident();
        if (ident.getType() != SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.LBRACK) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node exp = Parse_Exp();
            if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RBRACK) {
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        }
        return new Node(SyntaxVarType.LVal, children);
    }

    public Node Parse_Cond() {
        ArrayList<Node> children = new ArrayList<>();
        Node lOrExp = Parse_LOrExp();
        if (lOrExp.getType() != SyntaxVarType.ILLEGAL) children.add(lOrExp);
        else {
            unread(lOrExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.Cond, children);
    }

    public Node Parse_ForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) children.add(lVal);
        else {
            unread(lVal.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != tokenType.ASSIGN) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        Node exp = Parse_Exp();
        if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.ForStmt, children);
    }

    public Node Parse_FormatString() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.STRCON) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        return new Node(SyntaxVarType.FormatString, children);
    }

    public Node Parse_AddExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node mulExp = Parse_MulExp();
        if (mulExp.getType() != SyntaxVarType.ILLEGAL) children.add(mulExp);
        else {
            unread(mulExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before = new Node(SyntaxVarType.AddExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case PLUS:
                case MINU:
                    before = new Node(SyntaxVarType.AddExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    Node mulExp1 = Parse_MulExp();
                    if (mulExp1.getType() != SyntaxVarType.ILLEGAL) children.add(mulExp1);
                    else {
                        unread(mulExp1.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    break;
                default:
                    unread();
                    return new Node(SyntaxVarType.AddExp, children);
            }
        }
    }

    public Node Parse_LOrExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node landExp = Parse_LAndExp();
        if (landExp.getType() != SyntaxVarType.ILLEGAL) children.add(landExp);
        else {
            unread(landExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before = new Node(SyntaxVarType.LOrExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() != tokenType.OR) {
                unread();
                break;
            } else {
                before = new Node(SyntaxVarType.LOrExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            }
            Node lAndExp = Parse_LAndExp();
            if (lAndExp.getType() != SyntaxVarType.ILLEGAL) children.add(lAndExp);
            else {
                unread(lAndExp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
        }
        return new Node(SyntaxVarType.LOrExp, children);
    }

    public Node Parse_PrimaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.LPARENT) {
            unread();
        } else {
            children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node exp = Parse_Exp();
            if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != tokenType.RPARENT) {
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            return new Node(SyntaxVarType.PrimaryExp, children);
        }
        Node lVal = Parse_LVal();
        if (lVal.getType() != SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            return new Node(SyntaxVarType.PrimaryExp, children);
        } else {
            unread(lVal.getSize());
        }
        Node number = Parse_Number();
        if (number.getType() != SyntaxVarType.ILLEGAL) {
            children.add(number);
            return new Node(SyntaxVarType.PrimaryExp, children);
        } else {
            unread(number.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
    }

    public Node Parse_Number() {
        ArrayList<Node> children = new ArrayList<>();
        Node intConst = Parse_IntConst();
        if (intConst.getType() != SyntaxVarType.ILLEGAL) children.add(intConst);
        else {
            unread(intConst.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        return new Node(SyntaxVarType.Number, children);
    }

    public Node Parse_IntConst() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        if (curToken.getType() != tokenType.INTCON) {
            unread();
            return new Node(SyntaxVarType.ILLEGAL, children);
        } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
        return new Node(SyntaxVarType.IntConst, children);
    }

    public Node Parse_UnaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node indent = Parse_Ident();
        if (indent.getType() != SyntaxVarType.ILLEGAL) {
            children.add(indent);
            read();
            if (curToken.getType() != tokenType.LPARENT) {
                unread();
                unread(new Node(SyntaxVarType.UnaryExp, children).getSize());
                children.clear();
            } else {
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node funcRParams = Parse_FuncRParams();
                if (funcRParams.getType() != SyntaxVarType.ILLEGAL) children.add(funcRParams);
                else unread(funcRParams.getSize());
                read();
                if (curToken.getType() != tokenType.RPARENT) {
                    unread();
                    return new Node(SyntaxVarType.ILLEGAL, children);
                } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                return new Node(SyntaxVarType.UnaryExp, children);
            }
        } else unread(indent.getSize());
        Node unaryOp = Parse_UnaryOp();
        if (unaryOp.getType() != SyntaxVarType.ILLEGAL) {
            children.add(unaryOp);
            Node unaryExp = Parse_UnaryExp();
            if (unaryExp.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp);
            else {
                unread(unaryExp.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
            return new Node(SyntaxVarType.UnaryExp, children);
        } else {
            unread(unaryOp.getSize());
        }
        Node primaryExp = Parse_PrimaryExp();
        if (primaryExp.getType() != SyntaxVarType.ILLEGAL) {
            children.add(primaryExp);
            return new Node(SyntaxVarType.UnaryExp, children);
        } else unread(primaryExp.getSize());
        return new Node(SyntaxVarType.ILLEGAL, children);
    }

    public Node Parse_UnaryOp() {
        ArrayList<Node> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case PLUS:
            case MINU:
            case NOT:
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                return new Node(SyntaxVarType.UnaryOp, children);
            default:
                unread();
                return new Node(SyntaxVarType.ILLEGAL, children);
        }
    }

    public Node Parse_MulExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node unaryExp = Parse_UnaryExp();
        if (unaryExp.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp);
        else {
            unread(unaryExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before;
        while (true) {
            read();
            switch (curToken.getType()) {
                case MULT:
                case DIV:
                case MOD:
                    before = new Node(SyntaxVarType.MulExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    Node unaryExp1 = Parse_UnaryExp();
                    if (unaryExp1.getType() != SyntaxVarType.ILLEGAL) children.add(unaryExp1);
                    else {
                        unread(unaryExp1.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    break;
                default:
                    unread();
                    return new Node(SyntaxVarType.MulExp, children);
            }
        }
    }

    public Node Parse_RelExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node addExp = Parse_AddExp();
        if (addExp.getType() != SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before = new Node(SyntaxVarType.RelExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case GEQ:
                case GRE:
                case LEQ:
                case LSS:
                    before = new Node(SyntaxVarType.RelExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    Node addExp1 = Parse_AddExp();
                    if (addExp1.getType() != SyntaxVarType.ILLEGAL) children.add(addExp1);
                    else {
                        unread(addExp1.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    break;
                default:
                    unread();
                    return new Node(SyntaxVarType.RelExp, children);
            }
        }
    }

    public Node Parse_EqExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node relExp = Parse_RelExp();
        if (relExp.getType() != SyntaxVarType.ILLEGAL) children.add(relExp);
        else {
            unread(relExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before = new Node(SyntaxVarType.EqExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case EQL:
                case NEQ:
                    before = new Node(SyntaxVarType.EqExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                    Node relExp1 = Parse_RelExp();
                    if (relExp1.getType() != SyntaxVarType.ILLEGAL) children.add(relExp1);
                    else {
                        unread(relExp1.getSize());
                        return new Node(SyntaxVarType.ILLEGAL, children);
                    }
                    break;
                default:
                    unread();
                    return new Node(SyntaxVarType.EqExp, children);
            }
        }
    }

    public Node Parse_LAndExp() {
        ArrayList<Node> children = new ArrayList<>();
        Node eqExp = Parse_EqExp();
        if (eqExp.getType() != SyntaxVarType.ILLEGAL) children.add(eqExp);
        else {
            unread(eqExp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        Node before = new Node(SyntaxVarType.LAndExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() == tokenType.AND) {
                before = new Node(SyntaxVarType.LAndExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
                Node eqExp1 = Parse_EqExp();
                if (eqExp1.getType() != SyntaxVarType.ILLEGAL) children.add(eqExp1);
                else {
                    unread(eqExp1.getSize());
                    return new Node(SyntaxVarType.ILLEGAL, children);
                }
            } else {
                unread();
                return new Node(SyntaxVarType.LAndExp, children);
            }
        }
    }

    public Node Parse_FuncRParams() {
        ArrayList<Node> children = new ArrayList<>();
        Node exp = Parse_Exp();
        if (exp.getType() != SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return new Node(SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != tokenType.COMMA) {
                unread();
                break;
            } else children.add(new TokenNode(SyntaxVarType.TOKEN, curToken, tokenStream.getWatchPoint()));
            Node exp1 = Parse_Exp();
            if (exp1.getType() != SyntaxVarType.ILLEGAL) children.add(exp1);
            else {
                unread(exp1.getSize());
                return new Node(SyntaxVarType.ILLEGAL, children);
            }
        }
        return new Node(SyntaxVarType.FuncRParams, children);
    }

}

