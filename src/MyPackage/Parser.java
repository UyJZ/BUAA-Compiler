package MyPackage;

import MyPackage.Parse.*;
import MyPackage.Symbol.FunSymbol;
import MyPackage.Symbol.Symbol;
import MyPackage.Symbol.SymbolTable;
import MyPackage.Symbol.MyValSymbol;

import java.util.ArrayList;

public class Parser {
    final Laxer laxer;
    static SymbolTable root;
    static SymbolTable curSymbolTable;
    boolean error;
    int loop;
    public Parser(Laxer laxer, SymbolTable root, boolean error) {
        this.laxer = laxer;
        if (!laxer.isEnd()) {
            laxer.nextToken();
        }
        this.root = root;
        curSymbolTable = root;
        this.error = error;
        loop = 0;
    }

    public CompUnit parseCompUnit() {
        ArrayList<Decl> decls = new ArrayList<>();
        while (!laxer.isEnd()) {
            if (laxer.getLexType() == null) {
                break;
            }
            if (laxer.getLexType() == LexType.CONSTTK) {
                printToken();
                laxer.nextToken();
                decls.add(parseConstDecl());
                continue;
            }
            String type = laxer.getToken();
            LexType lexType = laxer.getLexType();
            laxer.nextToken();
            String name = laxer.getToken();
            if (laxer.getLexType() == LexType.MAINTK) {
                OutPut.print(lexType + " " + type);
                printToken();
                laxer.nextToken();
                decls.add(parseMainFuncDef(type));
                continue;
            }
            laxer.nextToken();
            if (laxer.getLexType() == LexType.LPARENT) {
                OutPut.print(lexType + " " + type);
                OutPut.print("<FuncType>");
                OutPut.print(LexType.IDENFR +  " " + name);
                decls.add(parseFuncDef(type, name));
            }
            else {
                OutPut.print(lexType + " " + type);
                OutPut.print(LexType.IDENFR +  " " + name);
                decls.add(parseVarDecl(type, name));
            }
        }
        OutPut.print("<CompUnit>");
        return new CompUnit(decls);
    }

    public ConstDecl parseConstDecl() {
        assert laxer.getLexType() == LexType.INTTK;
        String type = laxer.getToken();
        printToken();
        laxer.nextToken();
        ArrayList<ConstDef> constDefs = new ArrayList<>();
        while (true) {
            constDefs.add(parseConstDef());
            if (laxer.getLexType() == LexType.COMMA) {
                printToken();
                laxer.nextToken();
            }
            else if (laxer.getLexType() == LexType.SEMICN) {
                printToken();
                laxer.nextToken();
                break;
            }
            else {
                if (error) {
                    OutPut.printError('i', laxer.getBeforeLine());
                }
                else {
                    assert false;
                }
                break;
            }
        }
        OutPut.print("<ConstDecl>");
        return new ConstDecl(type, constDefs);
    }

    public ConstDef parseConstDef() {
        String ident = laxer.getToken();
        assert laxer.getLexType() == LexType.IDENFR;
        int line = laxer.getLine();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        printToken();
        laxer.nextToken();
        int level = 0;
        while (laxer.getLexType() == LexType.LBRACK) {
            printToken();
            laxer.nextToken();
            constExps.add(parseConstExp());
            if (error && laxer.getLexType() !=  LexType.RBRACK) {
                OutPut.printError('k', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() ==  LexType.RBRACK;
                printToken();
                laxer.nextToken();
            }
            level++;
        }
        if (curSymbolTable.contain(ident) && error) {
            OutPut.printError('b', line);
        }
        else {
            curSymbolTable.addSymbol(ident, new MyValSymbol(ident, line, true, "int", level));
        }
        assert laxer.getLexType() == LexType.ASSIGN;
        printToken();
        laxer.nextToken();
        ConstInitVal constInitVal = parseConstInitVal();
        OutPut.print("<ConstDef>");
        return new ConstDef(constExps, constInitVal, ident);
    }

    public ConstInitVal parseConstInitVal() {
        ConstInitVal constInitVal;
        if (laxer.getLexType() == LexType.LBRACE) {
            ArrayList<ConstInitVal> constInitValS = new ArrayList<>();
            while (true) {
                if (laxer.getLexType() == LexType.RBRACE) {
                    printToken();
                    laxer.nextToken();
                    constInitVal = new ConstInitVal(constInitValS);
                    break;
                }
                else if (laxer.getLexType() == LexType.COMMA || laxer.getLexType() == LexType.LBRACE) {
                    printToken();
                    laxer.nextToken();
                    constInitValS.add(parseConstInitVal());
                }
                else {
                    assert false;
                }
            }
        }
        else {
            constInitVal = new ConstInitVal(parseConstExp());
        }
        OutPut.print("<ConstInitVal>");
        return constInitVal;
    }

    public VarDecl parseVarDecl(String type, String name) {
        ArrayList<VarDef> varDefs = new ArrayList<>();
        varDefs.add(parseVarDef(name));
        while(true) {
            if (laxer.getLexType() == LexType.SEMICN) {
                printToken();
                laxer.nextToken();
                break;
            }
            else if (laxer.getLexType() == LexType.COMMA) {
                printToken();
                laxer.nextToken();
                varDefs.add(parseVarDef());
            }
            else {
                if (error) {
                    OutPut.printError('i', laxer.getBeforeLine());
                }
                else {
                    assert false;
                }
                break;
            }
        }
        OutPut.print("<VarDecl>");
        return new VarDecl(type, varDefs);
    }

    public VarDef parseVarDef(String ident) {
        ArrayList<ConstExp> constExps = new ArrayList<>();
        InitVal initVal = null;
        int line = laxer.getBeforeLine();
        int level = 0;
        while (laxer.getLexType() == LexType.LBRACK) {
            level++;
            printToken();
            laxer.nextToken();
            constExps.add(parseConstExp());
            if (error && laxer.getLexType() !=  LexType.RBRACK) {
                OutPut.printError('k', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() ==  LexType.RBRACK;
                printToken();
                laxer.nextToken();
            }
        }
        if (curSymbolTable.contain(ident) && error) {
            OutPut.printError('b', line);
        }
        else {
            curSymbolTable.addSymbol(ident, new MyValSymbol(ident, line, false, "int", level));
        }
        if (laxer.getLexType() == LexType.ASSIGN) {
            printToken();
            laxer.nextToken();
            initVal = parseInitVal();
        }
        OutPut.print("<VarDef>");
        return new VarDef(constExps, initVal, ident);
    }

    public VarDef parseVarDef() {
        String ident = laxer.getToken();
        assert laxer.getLexType() == LexType.IDENFR;
        printToken();
        laxer.nextToken();
        return parseVarDef(ident);
    }

    public InitVal parseInitVal() {
        InitVal initVal;
        if (laxer.getLexType() == LexType.LBRACE) {
            ArrayList<InitVal> initValS = new ArrayList<>();
            while (true) {
                if (laxer.getLexType() == LexType.RBRACE) {
                    printToken();
                    laxer.nextToken();
                    break;
                }
                else if (laxer.getLexType() == LexType.COMMA || laxer.getLexType() == LexType.LBRACE) {
                    printToken();
                    laxer.nextToken();
                    initValS.add(parseInitVal());
                }
                else {
                    assert false;
                }
            }
            initVal = new InitVal(initValS);
        }
        else {
            initVal = new InitVal(parseExp());
        }
        OutPut.print("<InitVal>");
        return initVal;
    }

    public FuncDef parseFuncDef(String type, String name) {
        FuncFParams funcFParams = null;
        int line = laxer.getBeforeLine();
        curSymbolTable = new SymbolTable(curSymbolTable);
        curSymbolTable.getPre().addTable(curSymbolTable);
        printToken();
        laxer.nextToken();
        if (laxer.getLexType() != LexType.RPARENT && laxer.getLexType() != LexType.LBRACE) {
            funcFParams = parseFuncFParams();
        }
        if (root.contain(name) && error) {
            OutPut.printError('b', line);
        }
        else {
            root.addSymbol(name, new FunSymbol(name, line, false, type, funcFParams));
        }
        if (error && laxer.getLexType() != LexType.RPARENT) {
            OutPut.printError('j', laxer.getBeforeLine());
        }
        else {
            assert laxer.getLexType() == LexType.RPARENT;
            printToken();
            laxer.nextToken();
        }
        Block block = parseBlock(true, type);
        OutPut.print("<FuncDef>");
        return new FuncDef(type, name, funcFParams, block);
    }

    public MainFuncDef parseMainFuncDef(String type) {
        assert laxer.getLexType() == LexType.LPARENT;
        printToken();
        laxer.nextToken();
        if (error && laxer.getLexType() != LexType.RPARENT) {
            OutPut.printError('j', laxer.getBeforeLine());
        }
        else {
            assert laxer.getLexType() == LexType.RPARENT;
            printToken();
            laxer.nextToken();
        }
        curSymbolTable = new SymbolTable(curSymbolTable);
        curSymbolTable.getPre().addTable(curSymbolTable);
        Block block = parseBlock(true, type);
        OutPut.print("<MainFuncDef>");
        return new MainFuncDef(block);
    }

    public FuncFParams parseFuncFParams() {
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        funcFParams.add(parseFuncFParam());
        while (laxer.getLexType() == LexType.COMMA) {
            printToken();
            laxer.nextToken();
            funcFParams.add(parseFuncFParam());
        }
        OutPut.print("<FuncFParams>");
        return new FuncFParams(funcFParams);
    }

    public FuncFParam parseFuncFParam() {
        assert laxer.getLexType() == LexType.INTTK;
        String type = laxer.getToken();
        printToken();
        laxer.nextToken();
        String name = laxer.getToken();
        assert laxer.getLexType() == LexType.IDENFR;
        int line = laxer.getLine();
        printToken();
        laxer.nextToken();
        int level = 0;
        if (laxer.getLexType() == LexType.LBRACK) {
            printToken();
            laxer.nextToken();
            level++;
            if (error && laxer.getLexType() !=  LexType.RBRACK) {
                OutPut.printError('k', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() ==  LexType.RBRACK;
                printToken();
                laxer.nextToken();
            }
        }
        ArrayList<ConstExp> constExps = new ArrayList<>();
        while (laxer.getLexType() == LexType.LBRACK) {
            printToken();
            laxer.nextToken();
            constExps.add(parseConstExp());
            if (error && laxer.getLexType() !=  LexType.RBRACK) {
                OutPut.printError('k', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() ==  LexType.RBRACK;
                printToken();
                laxer.nextToken();
            }
            level++;
        }
        if (curSymbolTable.contain(name) && error) {
            OutPut.printError('b', line);
        }
        else {
            curSymbolTable.addSymbol(name, new MyValSymbol(name, line, false, type, level));
        }
        OutPut.print("<FuncFParam>");
        return new FuncFParam(type, name, constExps, level);
    }

    public Block parseBlock(boolean isFunc, String type) {
        if (!isFunc) {
            curSymbolTable = new SymbolTable(curSymbolTable);
            curSymbolTable.getPre().addTable(curSymbolTable);
        }
        assert laxer.getLexType() == LexType.LBRACE;
        printToken();
        laxer.nextToken();
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (laxer.getLexType() != LexType.RBRACE) {
            blockItems.add(parseBlockItem(type != null && type.equals("void")));
        }
        assert laxer.getLexType() == LexType.RBRACE;
        if (error && isFunc && type.equals("int") && ((blockItems.size() > 0
                && (blockItems.get(blockItems.size() - 1).getStmt() == null ||
                blockItems.get(blockItems.size() - 1).getStmt().getType() != 8)) || blockItems.size() == 0)) {
            OutPut.printError('g', laxer.getLine());
        }
        printToken();
        laxer.nextToken();
        OutPut.print("<Block>");
        curSymbolTable = curSymbolTable.getPre();
        return new Block(blockItems);
    }

    public BlockItem parseBlockItem(boolean isVoid) {
        if (laxer.getLexType() == LexType.CONSTTK) {
            printToken();
            laxer.nextToken();
            ConstDecl constDecl = parseConstDecl();
            return new BlockItem(constDecl);
        }
        else if (laxer.getLexType() == LexType.INTTK) {
            String type = laxer.getToken();
            printToken();
            laxer.nextToken();
            String name = laxer.getToken();
            printToken();
            laxer.nextToken();
            VarDecl varDecl = parseVarDecl(type, name);
            return new BlockItem(varDecl);
        }
        else {
            Stmt stmt = parseStmt(isVoid);
            return new BlockItem(stmt);
        }
    }

    public Stmt parseStmt(boolean isVoid) {
        Stmt stmt;
        if (laxer.getLexType() == LexType.SEMICN) {
            Exp exp = null;
            printToken();
            laxer.nextToken();
            stmt = new Stmt(exp);
        }
        else if (error && laxer.getLexType() == LexType.RBRACE) {
            OutPut.printError('i', laxer.getBeforeLine());
            Exp exp = null;
            stmt = new Stmt(exp);
        }
        else if (laxer.getLexType() == LexType.LBRACE) {
            String string = null;
            if (isVoid) {
                string = "void";
            }
            Block block = parseBlock(false, string);
            stmt = new Stmt(block);
        }
        else if (laxer.getLexType() == LexType.IFTK) {
            Stmt stmt1;
            Stmt stmt2;
            printToken();
            laxer.nextToken();
            assert laxer.getLexType() == LexType.LPARENT;
            printToken();
            laxer.nextToken();
            Cond cond = parseCond();
            if (error && laxer.getLexType() != LexType.RPARENT) {
                OutPut.printError('j', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.RPARENT;
                printToken();
                laxer.nextToken();
            }
            stmt1 = parseStmt(isVoid);
            if (laxer.getLexType() == LexType.ELSETK) {
                printToken();
                laxer.nextToken();
                stmt2 = parseStmt(isVoid);
                stmt = new Stmt(cond, stmt1, stmt2);
            }
            else {
                stmt = new Stmt(cond, stmt1);
            }
        }
        else if (laxer.getLexType() == LexType.FORTK) {
            ForStmt forStmt1 = null;
            Cond cond = null;
            ForStmt forStmt2 = null;
            Stmt stmt1;
            loop++;
            printToken();
            laxer.nextToken();
            assert laxer.getLexType() == LexType.LPARENT;
            printToken();
            laxer.nextToken();
            if (laxer.getLexType() != LexType.SEMICN) {
                forStmt1 = parseForStmt();
            }
            assert laxer.getLexType() == LexType.SEMICN;
            printToken();
            laxer.nextToken();
            if (laxer.getLexType() != LexType.SEMICN) {
                cond = parseCond();
            }
            assert laxer.getLexType() == LexType.SEMICN;
            printToken();
            laxer.nextToken();
            if (laxer.getLexType() != LexType.RPARENT) {
                forStmt2 = parseForStmt();
            }
            assert laxer.getLexType() == LexType.RPARENT;
            printToken();
            laxer.nextToken();
            stmt1 = parseStmt(isVoid);
            loop--;
            stmt = new Stmt(forStmt1, cond, forStmt2, stmt1);
        }
        else if (laxer.getLexType() == LexType.BREAKTK) {
            if (error && loop == 0) {
                OutPut.printError('m', laxer.getLine());
            }
            printToken();
            laxer.nextToken();
            if (error && laxer.getLexType() != LexType.SEMICN) {
                OutPut.printError('i', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.SEMICN;
                printToken();
                laxer.nextToken();
            }
            stmt = new Stmt(6);
        }
        else if (laxer.getLexType() == LexType.CONTINUETK) {
            if (error && loop == 0) {
                OutPut.printError('m', laxer.getLine());
            }
            printToken();
            laxer.nextToken();
            if (error && laxer.getLexType() != LexType.SEMICN) {
                OutPut.printError('i', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.SEMICN;
                printToken();
                laxer.nextToken();
            }
            stmt = new Stmt(7);
        }
        else if (laxer.getLexType() == LexType.RETURNTK) {
            Exp exp = null;
            int line = laxer.getLine();
            printToken();
            laxer.nextToken();
            int op = 11;
            if (laxer.getLexType() != LexType.SEMICN && laxer.getLexType() != LexType.RBRACE) {
                exp = parseExp();
                op = 8;
            }
            if (error && laxer.getLexType() != LexType.SEMICN) {
                OutPut.printError('i', laxer.getBeforeLine());
            }
            else {
                if (error && laxer.getLexType() != LexType.SEMICN) {
                    OutPut.printError('i', laxer.getBeforeLine());
                }
                else {
                    assert laxer.getLexType() == LexType.SEMICN;
                    printToken();
                    laxer.nextToken();
                }
            }
            if (error && isVoid && op == 10) {
                OutPut.printError('f', line);
            }
            stmt = new Stmt(op, exp, line);
        }
        else if (laxer.getLexType() == LexType.PRINTFTK) {
            int line = laxer.getLine();
            printToken();
            laxer.nextToken();
            assert laxer.getLexType() == LexType.LPARENT;
            printToken();
            laxer.nextToken();
            String string = laxer.getToken();
            int num = 0;
            if (error) {
                num = parseString(string, line);
            }
            printToken();
            laxer.nextToken();
            ArrayList<Exp> exps = new ArrayList<>();
            while (laxer.getLexType() == LexType.COMMA) {
                printToken();
                laxer.nextToken();
                exps.add(parseExp());
            }
            if (error && num != exps.size()) {
                OutPut.printError('l', line);
            }
            if (error && laxer.getLexType() != LexType.RPARENT) {
                OutPut.printError('j', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.RPARENT;
                printToken();
                laxer.nextToken();
            }
            if (error && laxer.getLexType() != LexType.SEMICN) {
                OutPut.printError('i', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.SEMICN;
                printToken();
                laxer.nextToken();
            }
            stmt = new Stmt(string, exps);
        }
        else if (laxer.getLexType() == LexType.IDENFR) {
            int line = laxer.getLine();
            String ident = laxer.getToken();
            LVal lVal = parseLVal(false);
            if (laxer.getLexType() == LexType.LPARENT) {
                if (error && !(curSymbolTable.search(ident) instanceof FunSymbol)) {
                    OutPut.printError('c', line);
                }
                Exp exp = parseExp(lVal);
                if (error && laxer.getLexType() != LexType.SEMICN) {
                    OutPut.printError('i', laxer.getBeforeLine());
                }
                else {
                    assert laxer.getLexType() == LexType.SEMICN;
                    printToken();
                    laxer.nextToken();
                }
                stmt = new Stmt(exp);
            }
            else if (laxer.getLexType() == LexType.ASSIGN) {
                if (error && !(curSymbolTable.search(ident) instanceof MyValSymbol)) {
                    OutPut.printError('c', line);
                }
                OutPut.print("<LVal>");
                printToken();
                laxer.nextToken();
                Symbol symbol = curSymbolTable.search(lVal.getIdent());
                if (error && symbol instanceof MyValSymbol) {
                    if (symbol.isConst()) {
                        OutPut.printError('h', line);
                    }
                }
                if (laxer.getLexType() == LexType.GETINTTK) {
                    printToken();
                    laxer.nextToken();
                    assert laxer.getLexType() == LexType.LPARENT;
                    printToken();
                    laxer.nextToken();
                    if (error && laxer.getLexType() != LexType.RPARENT) {
                        OutPut.printError('j', laxer.getBeforeLine());
                    }
                    else {
                        assert laxer.getLexType() == LexType.RPARENT;
                        printToken();
                        laxer.nextToken();
                    }
                    if (error && laxer.getLexType() != LexType.SEMICN) {
                        OutPut.printError('i', laxer.getBeforeLine());
                    }
                    else {
                        assert laxer.getLexType() == LexType.SEMICN;
                        printToken();
                        laxer.nextToken();
                    }
                    stmt = new Stmt(lVal);
                }
                else {
                    Exp exp = parseExp();
                    if (error && laxer.getLexType() != LexType.SEMICN) {
                        OutPut.printError('i', laxer.getBeforeLine());
                    }
                    else {
                        assert laxer.getLexType() == LexType.SEMICN;
                        printToken();
                        laxer.nextToken();
                    }
                    stmt = new Stmt(lVal, exp);
                }
            }
            else {
                if (error && !(curSymbolTable.search(ident) instanceof MyValSymbol)) {
                    OutPut.printError('c', line);
                }
                OutPut.print("<LVal>");
                Exp exp = parseExp(lVal);
                if (error && laxer.getLexType() != LexType.SEMICN) {
                    OutPut.printError('i', laxer.getBeforeLine());
                }
                else {
                    assert laxer.getLexType() == LexType.SEMICN;
                    printToken();
                    laxer.nextToken();
                }
                stmt = new Stmt(exp);
            }
        }
        else {
            Exp exp = parseExp();
            if (error && laxer.getLexType() != LexType.SEMICN) {
                OutPut.printError('i', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.SEMICN;
                printToken();
                laxer.nextToken();
            }
            stmt = new Stmt(exp);
        }
        OutPut.print("<Stmt>");
        return stmt;
    }

    public ForStmt parseForStmt() {
        LVal lVal = parseLVal(true);
        assert laxer.getLexType() == LexType.ASSIGN;
        printToken();
        laxer.nextToken();
        Exp exp = parseExp();
        OutPut.print("<ForStmt>");
        return new ForStmt(lVal, exp);
    }

    public Exp parseExp() {
        AddExp addExp = parseAddExp();
        OutPut.print("<Exp>");
        return new Exp(addExp);
    }

    public Exp parseExp(LVal lVal) {
        AddExp addExp = parseAddExp(lVal);
        OutPut.print("<Exp>");
        return new Exp(addExp);
    }

    public Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        OutPut.print("<Cond>");
        return new Cond(lOrExp);
    }

    public LVal parseLVal(Boolean op) {
        assert laxer.getLexType() == LexType.IDENFR;
        String ident = laxer.getToken();
        if (error && !(curSymbolTable.search(ident) instanceof MyValSymbol)) {
            if (op) {
                OutPut.printError('c', laxer.getLine());
            }
        }
        printToken();
        laxer.nextToken();
        ArrayList<Exp> exps = new ArrayList<>();
        while (laxer.getLexType() == LexType.LBRACK) {
            printToken();
            laxer.nextToken();
            exps.add(parseExp());
            if (error && laxer.getLexType() !=  LexType.RBRACK) {
                OutPut.printError('k', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() ==  LexType.RBRACK;
                printToken();
                laxer.nextToken();
            }
        }
        if (op) {
            OutPut.print("<LVal>");
        }
        return new LVal(ident, exps);
    }

    public PrimaryExp parsePrimaryExp() {
        PrimaryExp primaryExp;
        if (laxer.getLexType() == LexType.LPARENT) {
            printToken();
            laxer.nextToken();
            Exp exp = parseExp();
            if (error && laxer.getLexType() != LexType.RPARENT) {
                OutPut.printError('j', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.RPARENT;
                printToken();
                laxer.nextToken();
            }
            primaryExp = new PrimaryExp(exp);
        }
        else if (laxer.getLexType() == LexType.INTCON) {
            int num = Integer.parseInt(laxer.getToken());
            printToken();
            laxer.nextToken();
            OutPut.print("<Number>");
            primaryExp = new PrimaryExp(num);
        }
        else {
            LVal lVal = parseLVal(true);
            primaryExp =  new PrimaryExp(lVal);
        }
        OutPut.print("<PrimaryExp>");
        return primaryExp;
    }

    public PrimaryExp parsePrimaryExp(LVal lVal) {
        PrimaryExp primaryExp =  new PrimaryExp(lVal);
        OutPut.print("<PrimaryExp>");
        return primaryExp;
    }

    public UnaryExp parseUnaryExp() {
        UnaryExp unaryExp = null;
        if (laxer.getLexType() == LexType.PLUS || laxer.getLexType() == LexType.MINU || laxer.getLexType() == LexType.NOT) {
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp1 = parseUnaryExp();
            unaryExp = new UnaryExp(unaryOp, unaryExp1);
        }
        else if (laxer.getLexType() == LexType.LPARENT || laxer.getLexType() == LexType.INTCON) {
            PrimaryExp primaryExp = parsePrimaryExp();
            unaryExp = new UnaryExp(primaryExp);
        }
        else if (laxer.getLexType() == LexType.IDENFR) {
            String ident = laxer.getToken();
            int line = laxer.getLine();
            FunSymbol funSymbol = null;
            printToken();
            laxer.nextToken();
            if (laxer.getLexType() == LexType.LPARENT) {
                if (error && !(curSymbolTable.search(ident) instanceof FunSymbol)) {
                    OutPut.printError('c', line);
                }
                else if (error) {
                    funSymbol = (FunSymbol)root.search(ident);
                }
                printToken();
                laxer.nextToken();
                FuncRParams funcRParams = null;
                int level = 0;
                if (laxer.getLexType() != LexType.RPARENT && laxer.getLexType() != LexType.SEMICN) {
                    funcRParams = parseFuncRParams(line, funSymbol);
                }
                if (funcRParams != null) {
                    level = funcRParams.getLevel();
                }
                if (error && funSymbol != null && funSymbol.getNumber() != level) {
                    OutPut.printError('d', line);
                }
                if (error && laxer.getLexType() != LexType.RPARENT) {
                    OutPut.printError('j', laxer.getBeforeLine());
                }
                else {
                    assert laxer.getLexType() == LexType.RPARENT;
                    printToken();
                    laxer.nextToken();
                }
                unaryExp = new UnaryExp(ident, funcRParams);
            }
            else {
                ArrayList<Exp> exps = new ArrayList<>();
                while (laxer.getLexType() == LexType.LBRACK) {
                    printToken();
                    laxer.nextToken();
                    exps.add(parseExp());
                    if (error && laxer.getLexType() !=  LexType.RBRACK) {
                        OutPut.printError('k', laxer.getBeforeLine());
                    }
                    else {
                        assert laxer.getLexType() ==  LexType.RBRACK;
                        printToken();
                        laxer.nextToken();
                    }
                }
                if (curSymbolTable.search(ident) == null && error) {
                    OutPut.printError('c', line);
                }
                LVal lVal = new LVal(ident, exps);
                PrimaryExp primaryExp = new PrimaryExp(lVal);
                OutPut.print("<LVal>");
                OutPut.print("<PrimaryExp>");
                unaryExp = new UnaryExp(primaryExp);
            }
        }
        else {
            assert false;
        }
        OutPut.print("<UnaryExp>");
        return unaryExp;
    }

    public UnaryExp parseUnaryExp(LVal lVal) {
        UnaryExp unaryExp;
        FunSymbol funSymbol = null;
        if (laxer.getLexType() == LexType.LPARENT) {
            int line = laxer.getBeforeLine();
            if (error && curSymbolTable.search(lVal.getIdent()) instanceof FunSymbol) {
                funSymbol = (FunSymbol)root.search(lVal.getIdent());
            }
            printToken();
            laxer.nextToken();
            FuncRParams funcRParams = null;
            int level = 0;
            if (laxer.getLexType() != LexType.RPARENT && laxer.getLexType() != LexType.SEMICN) {
                funcRParams = parseFuncRParams(line, funSymbol);
            }
            if (funcRParams != null) {
                level = funcRParams.getLevel();
            }
            if (error && funSymbol != null && funSymbol.getNumber() != level) {
                OutPut.printError('d', line);
            }
            if (error && laxer.getLexType() != LexType.RPARENT) {
                OutPut.printError('j', laxer.getBeforeLine());
            }
            else {
                assert laxer.getLexType() == LexType.RPARENT;
                printToken();
                laxer.nextToken();
            }
            unaryExp = new UnaryExp(lVal.getIdent(), funcRParams);
        }
        else {

            unaryExp = new UnaryExp(parsePrimaryExp(lVal));
        }
        OutPut.print("<UnaryExp>");
        return unaryExp;
    }


    public UnaryOp parseUnaryOp() {
        String op = laxer.getToken();
        printToken();
        laxer.nextToken();
        OutPut.print("<UnaryOp>");
        return new UnaryOp(op);
    }

    public FuncRParams parseFuncRParams(int line, FunSymbol funSymbol) {
        ArrayList<Exp> exps = new ArrayList<>();
        int max;
        if (funSymbol == null) {
            max = 0;
        }
        else {
            max = funSymbol.getNumber();
        }
        int index = 0;
        Exp exp = parseExp();
        exps.add(exp);
        if (index < max && funSymbol.getLevel(index) != exp.getLevel()) {
            OutPut.printError('e', line);
        }
        index++;
        while (laxer.getLexType() == LexType.COMMA) {
            printToken();
            laxer.nextToken();
            exp = parseExp();
            exps.add(exp);
            if (index < max && funSymbol.getLevel(index) != exp.getLevel()) {
                OutPut.printError('e', line);
            }
            index++;
        }
        OutPut.print("<FuncRParams>");
        return new FuncRParams(exps);
    }

    public MulExp parseMulExp() {
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        unaryExps.add(parseUnaryExp());
        OutPut.print("<MulExp>");
        while (laxer.getLexType() == LexType.MULT || laxer.getLexType() == LexType.DIV || laxer.getLexType() == LexType.MOD) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            unaryExps.add(parseUnaryExp());
            OutPut.print("<MulExp>");
        }
        return new MulExp(unaryExps, op);
    }

    public MulExp parseMulExp(LVal lVal) {
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        unaryExps.add(parseUnaryExp(lVal));
        OutPut.print("<MulExp>");
        while (laxer.getLexType() == LexType.MULT || laxer.getLexType() == LexType.DIV || laxer.getLexType() == LexType.MOD) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            unaryExps.add(parseUnaryExp());
            OutPut.print("<MulExp>");
        }
        return new MulExp(unaryExps, op);
    }

    public AddExp parseAddExp() {
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        mulExps.add(parseMulExp());
        OutPut.print("<AddExp>");
        while (laxer.getLexType() == LexType.PLUS || laxer.getLexType() == LexType.MINU) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            mulExps.add(parseMulExp());
            OutPut.print("<AddExp>");
        }
        return new AddExp(mulExps, op);
    }

    public AddExp parseAddExp(LVal lVal) {
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        mulExps.add(parseMulExp(lVal));
        OutPut.print("<AddExp>");
        while (laxer.getLexType() == LexType.PLUS || laxer.getLexType() == LexType.MINU) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            mulExps.add(parseMulExp());
            OutPut.print("<AddExp>");
        }
        return new AddExp(mulExps, op);
    }

    public RelExp parseRelExp() {
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        addExps.add(parseAddExp());
        OutPut.print("<RelExp>");
        while (laxer.getLexType() == LexType.LSS || laxer.getLexType() == LexType.LEQ ||
                laxer.getLexType() == LexType.GRE || laxer.getLexType() == LexType.GEQ) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            addExps.add(parseAddExp());
            OutPut.print("<RelExp>");
        }
        return new RelExp(addExps, op);
    }

    public EqExp parseEqExp() {
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<String> op = new ArrayList<>();
        relExps.add(parseRelExp());
        OutPut.print("<EqExp>");
        while (laxer.getLexType() == LexType.EQL || laxer.getLexType() == LexType.NEQ) {
            op.add(laxer.getToken());
            printToken();
            laxer.nextToken();
            relExps.add(parseRelExp());
            OutPut.print("<EqExp>");
        }
        return new EqExp(relExps, op);
    }

    public LAndExp parseLAndExp() {
        ArrayList<EqExp> eqExps = new ArrayList<>();
        eqExps.add(parseEqExp());
        OutPut.print("<LAndExp>");
        while (laxer.getLexType() == LexType.AND) {
            printToken();
            laxer.nextToken();
            eqExps.add(parseEqExp());
            OutPut.print("<LAndExp>");
        }
        return new LAndExp(eqExps);
    }

    public LOrExp parseLOrExp() {
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        OutPut.print("<LOrExp>");
        while (laxer.getLexType() == LexType.OR) {
            printToken();
            laxer.nextToken();
            lAndExps.add(parseLAndExp());
            OutPut.print("<LOrExp>");
        }
        return new LOrExp(lAndExps);
    }

    public ConstExp parseConstExp() {
        AddExp addExp = parseAddExp();
        OutPut.print("<ConstExp>");
        return new ConstExp(addExp);
    }

    public void printToken() {
        if (laxer.getLexType() != null) {
            OutPut.print(laxer.getLexType() + " " + laxer.getToken());
        }
    }

    public int parseString(String string, int line) {
        int index = 1;
        int ans = 0;
        boolean op = true;
        while (string.charAt(index) != '"') {
            if (string.charAt(index) == '%' ) {
                if (string.charAt(index+1) != 'd' && op && error) {
                    OutPut.printError('a', line);
                    op = false;
                }
                else if (string.charAt(index+1) == 'd') {
                    ans++;
                }
            }
            else if (string.charAt(index) == '\\' && string.charAt(index+1) != 'n' && op && error) {
                OutPut.printError('a', line);
                op = false;
            }
            else if (!(string.charAt(index) == 32 || string.charAt(index) == 33 ||
                    string.charAt(index) >= 40 && string.charAt(index) <= 126) && op && error) {
                OutPut.printError('a', laxer.getLine());
                op = false;
            }
            index++;
        }
        return ans;
    }

    public static SymbolTable getRoot() {
        return root;
    }

    public static SymbolTable getCurSymbolTable() {
        return curSymbolTable;
    }
}
