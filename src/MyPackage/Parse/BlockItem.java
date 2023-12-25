package MyPackage.Parse;

public class BlockItem {
    private int type;
    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl) {
        type = 0;
        stmt = null;
        this.decl = decl;
    }

    public BlockItem(Stmt stmt) {
        type = 1;
        this.stmt = stmt;
        decl = null;
    }

    public Stmt getStmt() {
        return stmt;
    }

    public void generateLlvm() {
        if (type == 1) {
            stmt.generateLlvm();
        }
        else {
            decl.generateLlvm(false);
        }
    }

}
