package FrontEnd.Parser;

import Config.Tasks;
import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.Lexer.Token;
import FrontEnd.Lexer.TokenStream;

import java.util.ArrayList;

public class Parser {

    private final TokenStream tokenStream;

    private final boolean isErrorManager;


    private Token curToken;

    public Parser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        this.isErrorManager = Tasks.isErrorHandle;
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

    public SynTreeNode parse() {
        return Parse_CompUnit();
    }

    public SynTreeNode Parse_CompUnit() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        while (true) {
            SynTreeNode decl = Parse_Decl();
            if (decl.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(decl);
            else {
                unread(decl.getSize());
                break;
            }
        }
        while (true) {
            SynTreeNode funcDef = Parse_FuncDef();
            if (funcDef.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcDef);
            else {
                unread(funcDef.getSize());
                break;
            }
        }
        SynTreeNode mainFuncDef = Parse_MainFuncDef();
        if (mainFuncDef.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(mainFuncDef);
        else {
            unread(mainFuncDef.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.CompUnit, children);
    }

    public SynTreeNode Parse_Decl() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode constDecl = Parse_ConstDecl();
        if (constDecl.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(constDecl);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Decl, children);
        } else unread(constDecl.getSize());
        SynTreeNode varDecl = Parse_VarDecl();
        if (varDecl.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(varDecl);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Decl, children);
        } else unread(varDecl.getSize());
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
    }

    public SynTreeNode Parse_FuncDef() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode funcType = Parse_FuncType();
        if (funcType.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcType);
        else {
            unread(funcType.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode ident = Parse_Ident();
        if (ident.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != Token.TokenType.LPARENT) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode funcFParams = Parse_FuncFParams();
        if (funcFParams.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcFParams);
        else unread(funcFParams.getSize());
        read();
        if (curToken.getType() != Token.TokenType.RPARENT) {
            unread();
            if (isErrorManager)
                ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.j));
            else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode block = Parse_Block();
        if (block.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncDef, children);
    }

    public SynTreeNode Parse_MainFuncDef() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.INTTK) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != Token.TokenType.MAINTK) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != Token.TokenType.LPARENT) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != Token.TokenType.RPARENT) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode block = Parse_Block();
        if (block.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(block);
        else {
            unread(block.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.MainFuncDef, children);
    }

    public SynTreeNode Parse_ConstDecl() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.CONSTTK) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode bType = Parse_BType();
        if (bType.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode constDef = Parse_ConstDef();
        if (constDef.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constDef);
        else {
            unread(constDef.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            constDef = Parse_ConstDef();
            if (constDef.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constDef);
            else {
                unread(constDef.getSize());
                break;
            }
        }
        read();
        if (curToken.getType() != Token.TokenType.SEMICN) {
            unread();
            if (isErrorManager) {
                ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.i));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstDecl, children);
            } else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstDecl, children);
        }
    }

    public SynTreeNode Parse_VarDecl() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode bType = Parse_BType();
        if (bType.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        int defNum = 0;
        SynTreeNode varDef = Parse_VarDef();
        if (varDef.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(varDef);
        else {
            unread(varDef.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        defNum++;
        while (true) {
            ArrayList<SynTreeNode> list = new ArrayList<>();
            read();
            //parse ,
            if (curToken.getType() != Token.TokenType.COMMA) {
                unread();
                break;
            } else list.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode varDef1 = Parse_VarDef();
            if (varDef1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) list.add(varDef1);
            else {
                unread(varDef1.getSize());
                break;
            }
            children.addAll(list);
            defNum++;
        }
        read();
        if (curToken.getType() != Token.TokenType.SEMICN) {
            if (curToken.getType() == Token.TokenType.LPARENT && defNum == 1) {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            unread();
            if (isErrorManager) {
                ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.i));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.VarDecl, children);
            } else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.VarDecl, children);
        }
    }

    public SynTreeNode Parse_BType() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.INTTK) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.BType, children);
    }

    public SynTreeNode Parse_ConstDef() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode ident = Parse_Ident();
        if (ident.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode constExp = Parse_ConstExp();
            if (constExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != Token.TokenType.RBRACK) {
                unread();
                if (isErrorManager)
                    ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.k));
                else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != Token.TokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstDef, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode constInitVal = Parse_ConstInitVal();
        if (constInitVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constInitVal);
        else {
            unread(constInitVal.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstDef, children);
    }

    public SynTreeNode Parse_Ident() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.IDENFR) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else return NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint());
    }

    public SynTreeNode Parse_ConstExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode addExp = Parse_AddExp();
        if (addExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstExp, children);
    }

    public SynTreeNode Parse_ConstInitVal() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode constExp = Parse_ConstExp();
        if (constExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constExp);
        else {
            unread(constExp.getSize());
            read();
            if (curToken.getType() != Token.TokenType.LBRACE) {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode constInitVal = Parse_ConstInitVal();
            if (constInitVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
                children.add(constInitVal);
                while (true) {
                    read();
                    if (curToken.getType() != Token.TokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    constInitVal = Parse_ConstInitVal();
                    if (constInitVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constInitVal);
                    else {
                        unread(constInitVal.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != Token.TokenType.RBRACE) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstInitVal, children);
            } else {
                unread(constInitVal.getSize());
                read();
                if (curToken.getType() == Token.TokenType.RBRACE) {
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstInitVal, children);
                }
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ConstInitVal, children);
    }

    public SynTreeNode Parse_VarDef() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode ident = Parse_Ident();
        if (ident.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode constExp = Parse_ConstExp();
            if (constExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != Token.TokenType.RBRACK) {
                unread();
                if (isErrorManager)
                    ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.k));
                else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        }
        read();
        if (curToken.getType() != Token.TokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.VarDef, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode initVal = Parse_InitVal();
        if (initVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(initVal);
        else {
            unread(initVal.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.VarDef, children);
    }

    public SynTreeNode Parse_InitVal() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode exp = Parse_Exp();
        if (exp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            read();
            if (curToken.getType() != Token.TokenType.LBRACE) {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode initVal = Parse_InitVal();
            if (initVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
                children.add(initVal);
                while (true) {
                    read();
                    if (curToken.getType() != Token.TokenType.COMMA) {
                        unread();
                        break;
                    } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    initVal = Parse_InitVal();
                    if (initVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(initVal);
                    else {
                        unread(initVal.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != Token.TokenType.RBRACE) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.InitVal, children);
            } else {
                unread(initVal.getSize());
                read();
                if (curToken.getType() == Token.TokenType.RBRACE) {
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.InitVal, children);
                }
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.InitVal, children);
    }

    public SynTreeNode Parse_Exp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode addExp = Parse_AddExp();
        if (addExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Exp, children);
    }

    public SynTreeNode Parse_FuncType() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() == Token.TokenType.VOIDTK || curToken.getType() == Token.TokenType.INTTK)
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        else {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncType, children);
    }

    public SynTreeNode Parse_FuncFParams() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode funcFParam = Parse_FuncFParam();
        if (funcFParam.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcFParam);
        else {
            unread(funcFParam.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            funcFParam = Parse_FuncFParam();
            if (funcFParam.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcFParam);
            else {
                unread(funcFParam.getSize());
                break;
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncFParams, children);
    }

    public SynTreeNode Parse_Block() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.LBRACE) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        while (true) {
            SynTreeNode blockItem = Parse_BlockItem();
            if (blockItem.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(blockItem);
            else {
                unread(blockItem.getSize());
                break;
            }
        }
        read();
        if (curToken.getType() != Token.TokenType.RBRACE) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Block, children);
    }

    public SynTreeNode Parse_FuncFParam() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode bType = Parse_BType();
        if (bType.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(bType);
        else {
            unread(bType.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode ident = Parse_Ident();
        if (ident.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != Token.TokenType.LBRACK) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncFParam, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        read();
        if (curToken.getType() != Token.TokenType.RBRACK) {
            unread();
            if (isErrorManager)
                ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.k));
            else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode constExp = Parse_ConstExp();
            if (constExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(constExp);
            else {
                unread(constExp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != Token.TokenType.RBRACK) {
                unread();
                if (isErrorManager)
                    ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.k));
                else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncFParam, children);
    }

    public SynTreeNode Parse_BlockItem() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode decl = Parse_Decl();
        if (decl.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(decl);
        else {
            unread(decl.getSize());
            SynTreeNode stmt = Parse_Stmt();
            if (stmt.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(stmt);
            else {
                unread(stmt.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.BlockItem, children);
    }

    private SynTreeNode Parse_SEMICN(ArrayList<SynTreeNode> children, SynTreeNode.SyntaxVarType type) {
        read();
        if (curToken.getType() != Token.TokenType.SEMICN) {
            unread();
            if (isErrorManager) {
                ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.i));
                return NodeGenerator.generateNode(type, children);
            } else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(type, children);
        }
    }

    public SynTreeNode Parse_Stmt() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case SEMICN -> {
                //Parse ';'
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ExpStmt, children);
            }
            case IFTK -> {
                //Parse 'if' '(' Cond ')' Stmt ['else' Stmt]
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != Token.TokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode cond = Parse_Cond();
                if (cond.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(cond);
                else {
                    unread(cond.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != Token.TokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode stmt1 = Parse_Stmt();
                if (stmt1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(stmt1);
                else {
                    unread(stmt1.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
                read();
                if (curToken.getType() != Token.TokenType.ELSETK) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.IfStmt, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode stmt2 = Parse_Stmt();
                if (stmt2.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(stmt2);
                else {
                    unread(stmt2.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.IfStmt, children);
            }
            case FORTK -> {
                //Parse  'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != Token.TokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode forStmt1 = Parse_ForStmt();
                if (forStmt1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(forStmt1);
                else unread(forStmt1.getSize());
                read();
                if (curToken.getType() != Token.TokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode cond1 = Parse_Cond();
                if (cond1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(cond1);
                else unread(cond1.getSize());
                read();
                if (curToken.getType() != Token.TokenType.SEMICN) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode forStmt2 = Parse_ForStmt();
                if (forStmt2.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(forStmt2);
                else unread(forStmt2.getSize());
                read();
                if (curToken.getType() != Token.TokenType.RPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode stmt3 = Parse_Stmt();
                if (stmt3.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(stmt3);
                else {
                    unread(stmt3.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ForLoopStmt, children);
            }
            case BREAKTK -> {
                // Parse 'break' ';'
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.BreakStmt);
            }
            case CONTINUETK -> {
                // Parse 'continue' ';'
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.ContinueStmt);
            }
            case RETURNTK -> {
                // Parse 'return' [Exp] ';'
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode exp1 = Parse_Exp();
                if (exp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp1);
                else unread(exp1.getSize());
                return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.ReturnStmt);
            }
            case PRINTFTK -> {
                // Parse 'printf' '(' [FormatString] [',' Exp] ')' ';'
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != Token.TokenType.LPARENT) {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode formatString = Parse_FormatString();
                if (formatString.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(formatString);
                else {
                    unread(formatString.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
                while (true) {
                    read();
                    if (curToken.getType() != Token.TokenType.COMMA) {
                        unread();
                        break;
                    } else
                        children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    SynTreeNode exp2 = Parse_Exp();
                    if (exp2.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp2);
                    else {
                        unread(exp2.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                read();
                if (curToken.getType() != Token.TokenType.RPARENT) {
                    unread();
                    if (isErrorManager)
                        ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.j));
                    else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.PrintfStmt);
            }
            default -> unread();
        }
        SynTreeNode lVal = Parse_LVal();
        if (lVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            read();
            if (curToken.getType() != Token.TokenType.ASSIGN) {
                unread();
                unread(NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Stmt, children).getSize());
                children.clear();
            } else {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                read();
                if (curToken.getType() != Token.TokenType.GETINTTK) {
                    //Parse LVal '=' Exp ';'
                    unread();
                    SynTreeNode exp3 = Parse_Exp();
                    if (exp3.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
                        children.add(exp3);
                        return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.AssignStmt);
                    } else {
                        unread(exp3.getSize());
                        unread(NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Stmt, children).getSize());
                        children.clear();
                    }
                } else {
                    //Parse LVal '=' 'getint' '(' ')' ';'
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != Token.TokenType.LPARENT) {
                        unread();
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    read();
                    if (curToken.getType() != Token.TokenType.RPARENT) {
                        unread();
                        if (isErrorManager)
                            ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.j));
                        else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.GetIntStmt);
                }
            }
        }
        SynTreeNode block = Parse_Block();
        //Parse Block
        if (block.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(block);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.BlockStmt, children);
        } else unread(block.getSize());
        SynTreeNode exp4 = Parse_Exp();
        //Parse Exp
        if (exp4.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(exp4);
            return Parse_SEMICN(children, SynTreeNode.SyntaxVarType.ExpStmt);
        } else unread(exp4.getSize());
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
    }

    public SynTreeNode Parse_LVal() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode ident = Parse_Ident();
        if (ident.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(ident);
        else {
            unread(ident.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.LBRACK) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode exp = Parse_Exp();
            if (exp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != Token.TokenType.RBRACK) {
                unread();
                if (isErrorManager)
                    ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.k));
                else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LVal, children);
    }

    public SynTreeNode Parse_Cond() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode lOrExp = Parse_LOrExp();
        if (lOrExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(lOrExp);
        else {
            unread(lOrExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Cond, children);
    }

    public SynTreeNode Parse_ForStmt() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode lVal = Parse_LVal();
        if (lVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(lVal);
        else {
            unread(lVal.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        read();
        if (curToken.getType() != Token.TokenType.ASSIGN) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        SynTreeNode exp = Parse_Exp();
        if (exp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ForStmt, children);
    }

    public SynTreeNode Parse_FormatString() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.STRCON) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else return NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint());
    }

    public SynTreeNode Parse_AddExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode mulExp = Parse_MulExp();
        if (mulExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(mulExp);
        else {
            unread(mulExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.AddExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case PLUS, MINU -> {
                    before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.AddExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    SynTreeNode mulExp1 = Parse_MulExp();
                    if (mulExp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(mulExp1);
                    else {
                        unread(mulExp1.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.AddExp, children);
                }
            }
        }
    }

    public SynTreeNode Parse_LOrExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode landExp = Parse_LAndExp();
        if (landExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(landExp);
        else {
            unread(landExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LOrExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.OR) {
                unread();
                break;
            } else {
                before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LOrExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            }
            SynTreeNode lAndExp = Parse_LAndExp();
            if (lAndExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(lAndExp);
            else {
                unread(lAndExp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LOrExp, children);
    }

    public SynTreeNode Parse_PrimaryExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.LPARENT) {
            unread();
        } else {
            children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode exp = Parse_Exp();
            if (exp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp);
            else {
                unread(exp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            read();
            if (curToken.getType() != Token.TokenType.RPARENT) {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.PrimaryExp, children);
        }
        SynTreeNode lVal = Parse_LVal();
        if (lVal.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(lVal);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.PrimaryExp, children);
        } else {
            unread(lVal.getSize());
        }
        SynTreeNode number = Parse_Number();
        if (number.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(number);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.PrimaryExp, children);
        } else {
            unread(number.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
    }

    public SynTreeNode Parse_Number() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode intConst = Parse_IntConst();
        if (intConst.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(intConst);
        else {
            unread(intConst.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.Number, children);
    }

    public SynTreeNode Parse_IntConst() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        if (curToken.getType() != Token.TokenType.INTCON) {
            unread();
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.IntConst, children);
    }

    public SynTreeNode Parse_UnaryExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode indent = Parse_Ident();
        if (indent.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(indent);
            read();
            if (curToken.getType() != Token.TokenType.LPARENT) {
                unread();
                unread(NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.UnaryExp, children).getSize());
                children.clear();
            } else {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode funcRParams = Parse_FuncRParams();
                if (funcRParams.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(funcRParams);
                else unread(funcRParams.getSize());
                read();
                if (curToken.getType() != Token.TokenType.RPARENT) {
                    unread();
                    if (isErrorManager)
                        ErrorList.AddError(new Error(children.get(children.size() - 1).getEndLine(), ErrorType.j));
                    else return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.UnaryExp, children);
            }
        } else unread(indent.getSize());
        SynTreeNode unaryOp = Parse_UnaryOp();
        if (unaryOp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(unaryOp);
            SynTreeNode unaryExp = Parse_UnaryExp();
            if (unaryExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(unaryExp);
            else {
                unread(unaryExp.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.UnaryExp, children);
        } else {
            unread(unaryOp.getSize());
        }
        SynTreeNode primaryExp = Parse_PrimaryExp();
        if (primaryExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) {
            children.add(primaryExp);
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.UnaryExp, children);
        } else unread(primaryExp.getSize());
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
    }

    public SynTreeNode Parse_UnaryOp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        read();
        switch (curToken.getType()) {
            case PLUS, MINU, NOT -> {
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.UnaryOp, children);
            }
            default -> {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
    }

    public SynTreeNode Parse_MulExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode unaryExp = Parse_UnaryExp();
        if (unaryExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(unaryExp);
        else {
            unread(unaryExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before;
        while (true) {
            read();
            switch (curToken.getType()) {
                case MULT, DIV, MOD -> {
                    before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.MulExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    SynTreeNode unaryExp1 = Parse_UnaryExp();
                    if (unaryExp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(unaryExp1);
                    else {
                        unread(unaryExp1.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.MulExp, children);
                }
            }
        }
    }

    public SynTreeNode Parse_RelExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode addExp = Parse_AddExp();
        if (addExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(addExp);
        else {
            unread(addExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.RelExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case GEQ, GRE, LEQ, LSS -> {
                    before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.RelExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    SynTreeNode addExp1 = Parse_AddExp();
                    if (addExp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(addExp1);
                    else {
                        unread(addExp1.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.RelExp, children);
                }
            }
        }
    }

    public SynTreeNode Parse_EqExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode relExp = Parse_RelExp();
        if (relExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(relExp);
        else {
            unread(relExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.EqExp, new ArrayList<>(children));
        while (true) {
            read();
            switch (curToken.getType()) {
                case EQL, NEQ -> {
                    before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.EqExp, new ArrayList<>(children));
                    children.clear();
                    children.add(before);
                    children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                    SynTreeNode relExp1 = Parse_RelExp();
                    if (relExp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(relExp1);
                    else {
                        unread(relExp1.getSize());
                        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                    }
                }
                default -> {
                    unread();
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.EqExp, children);
                }
            }
        }
    }

    public SynTreeNode Parse_LAndExp() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode eqExp = Parse_EqExp();
        if (eqExp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(eqExp);
        else {
            unread(eqExp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        SynTreeNode before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LAndExp, new ArrayList<>(children));
        while (true) {
            read();
            if (curToken.getType() == Token.TokenType.AND) {
                before = NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LAndExp, new ArrayList<>(children));
                children.clear();
                children.add(before);
                children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
                SynTreeNode eqExp1 = Parse_EqExp();
                if (eqExp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(eqExp1);
                else {
                    unread(eqExp1.getSize());
                    return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
                }
            } else {
                unread();
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.LAndExp, children);
            }
        }
    }

    public SynTreeNode Parse_FuncRParams() {
        ArrayList<SynTreeNode> children = new ArrayList<>();
        SynTreeNode exp = Parse_Exp();
        if (exp.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp);
        else {
            unread(exp.getSize());
            return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
        }
        while (true) {
            read();
            if (curToken.getType() != Token.TokenType.COMMA) {
                unread();
                break;
            } else children.add(NodeGenerator.generateToken(curToken, tokenStream.getWatchPoint()));
            SynTreeNode exp1 = Parse_Exp();
            if (exp1.getType() != SynTreeNode.SyntaxVarType.ILLEGAL) children.add(exp1);
            else {
                unread(exp1.getSize());
                return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.ILLEGAL, children);
            }
        }
        return NodeGenerator.generateNode(SynTreeNode.SyntaxVarType.FuncRParams, children);
    }

}

