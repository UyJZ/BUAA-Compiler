package MyPackage.Parse;

import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.BrLlvm;
import MyPackage.IR.Instruction.CallLlvm;
import MyPackage.IR.Instruction.RetLlvm;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.OutPut;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;
import MyPackage.Symbol.SymbolTable;


import java.util.ArrayList;

public class Stmt {
    private int type;
    private LVal lVal;
    private ArrayList<Exp> exps;
    private Block block;
    private Cond cond;
    private Stmt stmt1;
    private Stmt stmt2;
    private ForStmt forStmt1;
    private ForStmt forStmt2;
    private String formatString;
    private int line;

    public Stmt(LVal lVal, Exp exp) {
        type = 0;
        this.lVal = lVal;
        exps = new ArrayList<>();
        exps.add(exp);
    }

    public Stmt(Exp exp) {
        type = 1;
        exps = new ArrayList<>();
        exps.add(exp);
    }

    public Stmt(Block block) {
        type = 2;
        this.block = block;
    }

    public Stmt(Cond cond, Stmt stmt) {
        type = 3;
        this.cond = cond;
        this.stmt1 = stmt;
    }

    public Stmt(Cond cond, Stmt stmt1, Stmt stmt2) {
        type = 4;
        this.cond = cond;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    public Stmt(ForStmt forStmt1, Cond cond, ForStmt forStmt2, Stmt stmt) {
        type = 5;
        this.stmt1 = stmt;
        this.forStmt1 = forStmt1;
        this.forStmt2 = forStmt2;
        this.cond = cond;
    }

    public Stmt(int type) {
        this.type = type;
    } // 6 break; 7 continue;

    public Stmt(int type, Exp exp, int line) {
        this.type = type;     // 有参数8 无参数11
        exps = new ArrayList<>();
        exps.add(exp);
        this.line = line;
    }

    public Stmt(LVal lVal) {
        type = 9;
        this.lVal = lVal;
    }

    public Stmt(String formatString, ArrayList<Exp> exps) {
        type = 10;
        this.formatString = formatString;
        this.exps = exps;
    }

    public int getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public void generateLlvm() {
        if (type == 1) {
            if (exps.get(0) != null) {
                exps.get(0).generateLlvm();
            }
        }
        else if (type == 2) {
            block.generateLlvm(false);
        }
        else if (type == 3) {
            int id = IRModule.getLabelID();
            cond.generateLlvm(id);
            IRModule.curFunction.newBlock(String.format("stmt_%d", id));
            stmt1.generateLlvm();
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("end_%d", id)));
            IRModule.curFunction.newBlock(String.format("end_%d", id));
        }
        else if (type == 4) {
            int id = IRModule.getLabelID();
            cond.generateLlvm(id);
            IRModule.curFunction.newBlock(String.format("stmt_%d", id));
            stmt1.generateLlvm();
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("else_end_%d", id)));
            IRModule.curFunction.newBlock(String.format("end_%d", id));
            stmt2.generateLlvm();
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("else_end_%d", id)));
            IRModule.curFunction.newBlock(String.format("else_end_%d", id));
        }
        else if (type == 5) {
            if (forStmt1 != null) {
                forStmt1.generateLlvm();
            }
            int id = IRModule.getLabelID();
            IRModule.addForId(id);
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("cond_%d", id)));
            IRModule.curFunction.newBlock(String.format("cond_%d", id));
            if (cond != null) {
                cond.generateLlvm(id);
            }
            else {
                IRModule.curFunction.getCurrentBlock().addInstruction(
                        new BrLlvm(String.format("stmt_%d", id)));
            }
            IRModule.curFunction.newBlock(String.format("stmt_%d", id));
            stmt1.generateLlvm();
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("forStmt_%d", id)));
            IRModule.curFunction.newBlock(String.format("forStmt_%d", id));
            if (forStmt2 != null) {
                forStmt2.generateLlvm();
            }
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("cond_%d", id)));
            IRModule.curFunction.newBlock(String.format("end_%d", id));
            IRModule.popForId();
        }
        else if (type == 6) {
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("end_%d", IRModule.getForId())));
            IRModule.curFunction.newBlock(String.format("break_%d", IRModule.getLabelID()));
        }
        else if (type == 7) {
            IRModule.curFunction.getCurrentBlock().addInstruction(
                    new BrLlvm(String.format("forStmt_%d", IRModule.getForId())));
            IRModule.curFunction.newBlock(String.format("continue_%d", IRModule.getLabelID()));
        }
        else if (type == 8) {
            RetLlvm retLlvm = new RetLlvm(Type.Int, 0);
            retLlvm.addOperand(exps.get(0).generateLlvm());
            IRModule.curFunction.getCurrentBlock().addInstruction(retLlvm);
            IRModule.getRegID();
        }
        else if (type == 9) {
            int id = IRModule.getRegID();
            CallLlvm callLlvm = new CallLlvm(Type.Reg, id, "getint");
            IRModule.curFunction.getCurrentBlock().addInstruction(callLlvm);
            SymbolTable symbolTable =  IRModule.getCurTable();
            Symbol symbol = symbolTable.search(lVal.getIdent());
            if (!(symbol instanceof MyValSymbol) || ((MyValSymbol) symbol).getReg() == null) {
                symbolTable = symbolTable.getPre();
                symbol = symbolTable.search(lVal.getIdent());
            }
            StoreLlvm storeLlvm = new StoreLlvm(callLlvm.getType(), callLlvm.getValue(), (MyValSymbol) symbol);
            storeLlvm.addOperand(callLlvm);
            Value value = lVal.generateLlvm((MyValSymbol) symbol);
            if (value ==null) {
                storeLlvm.addOperand(((MyValSymbol)symbol).getReg());
            }
            else {
                storeLlvm.addOperand(value);
            }
            IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
        }
        else if (type == 10) {
            ArrayList<Value> list = new ArrayList<>();
            for (int i = 0; i < exps.size(); i++) {
                list.add(exps.get(i).generateLlvm());
            }
            int index = 0;
            for (int i = 1; i < formatString.length() - 1; i++) {
                if (formatString.charAt(i) == '%' && formatString.charAt(i+1) == 'd') {
                    CallLlvm callLlvm = new CallLlvm(Type.Void, 0, "putint");
                    callLlvm.addOperand(list.get(index));
                    if (list.get(index).getType().equals(Type.Reg)) {
                        list.get(index).addUse(new Use(list.get(index), callLlvm, callLlvm.getPos()));
                    }
                    IRModule.curFunction.getCurrentBlock().addInstruction(callLlvm);
                    i++;
                    index++;
                }
                /* else if (formatString.charAt(i) == '\\' && formatString.charAt(i+1) == 'n') {
                    CallLlvm callLlvm = new CallLlvm(Type.Void, 0, "putch");
                    callLlvm.addOperand(new Value(Type.MyConst, '\n'));
                    IRModule.curFunction.getCurrentBlock().addInstruction(callLlvm);
                    i++;
                } */
                else {
                    CallLlvm callLlvm = new CallLlvm(Type.Void, 0, "putch");
                    callLlvm.addOperand(new Value(Type.MyConst, formatString.charAt(i)));
                    IRModule.curFunction.getCurrentBlock().addInstruction(callLlvm);
                }
            }
        }
        else if (type == 11) {
            IRModule.curFunction.getCurrentBlock().addInstruction(new RetLlvm(Type.Void, 0));
            IRModule.getRegID();
        }
        else if (type == 0) {
            SymbolTable symbolTable =  IRModule.getCurTable();
            Symbol symbol = symbolTable.search(lVal.getIdent());
            if (!(symbol instanceof MyValSymbol) || ((MyValSymbol) symbol).getReg() == null) {
                symbolTable = symbolTable.getPre();
                symbol = symbolTable.search(lVal.getIdent());
            }
            Value value = exps.get(0).generateLlvm();
            Value value1 = lVal.generateLlvm((MyValSymbol) symbol);
            StoreLlvm storeLlvm = new StoreLlvm(value.getType(), value.getValue(), (MyValSymbol)symbol);
            storeLlvm.addOperand(value);
            if (value1 == null) {
                storeLlvm.addOperand(((MyValSymbol) symbol).getReg());
            }
            else {
                storeLlvm.addOperand(value1);
            }
            IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
        }
    }
}
