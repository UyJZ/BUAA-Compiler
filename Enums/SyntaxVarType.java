package Enums;

public enum SyntaxVarType {
    CompUnit, Decl, FuncDef, MainFuncDef, ConstDecl, VarDecl, ConstDef, BType,
    ConstExp, ConstInitVal, VarDef, InitVal, Exp, FuncType, FuncFParams, Block, FuncFParam, BlockItem,
    Stmt, LVal, Cond, ForStmt, AddExp, LOrExp, PrimaryExp, Number, IntConst, UnaryExp,
    UnaryOp, MulExp, RelExp, EqExp, LAndExp, ILLEGAL, TOKEN, FuncRParams,
    AssignStmt("Stmt"), ExpStmt("Stmt"), IfStmt("Stmt"), ForLoopStmt("Stmt"), BreakStmt("Stmt"),
    ContinueStmt("Stmt"), ReturnStmt("Stmt"), GetIntStmt("Stmt"), PrintfStmt("Stmt"),
    BlockStmt("Stmt");

    private final String typeName;

    private SyntaxVarType(String typeName) {
        this.typeName = typeName;
    }

    private SyntaxVarType() {
        this.typeName = this.name();
    }

    @Override
    public String toString() {
        return typeName;
    }
}
