package FrontEnd.AbsSynTreeNodes.Stmt;

import FrontEnd.AbsSynTreeNodes.SynTreeNode;
import FrontEnd.AbsSynTreeNodes.TokenSynTreeNode;
import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Instr.CallInstr;
import IR_LLVM.LLVM_Types.VoidType;

import java.util.ArrayList;

public class PrintfStmt extends Stmt {

    private TokenSynTreeNode formatStringNode;

    private int OutputNums = 0;

    public PrintfStmt(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        for (SynTreeNode n : children)
            if (n instanceof TokenSynTreeNode && ((TokenSynTreeNode) n).isFormatString())
                formatStringNode = (TokenSynTreeNode) n;
            else if (n instanceof Exp) OutputNums++;
    }

    @Override
    public void checkError() {
        if (OutputNums != formatStringNode.getParameterNum())
            ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.l));
        super.checkError();
    }

    @Override
    public LLVM_Value genLLVMir() {
        ArrayList<LLVM_Value> instrs = new ArrayList<>();
        for (SynTreeNode n : children) {
            if (n instanceof Exp) instrs.add(n.genLLVMir());
        }
        String s = formatStringNode.getValue();
        s = s.substring(1, s.length() - 1);
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == '%' && s.charAt(i + 1) == 'd') {
                ArrayList<LLVM_Value> p = new ArrayList<>();
                LLVM_Value v = instrs.get(start++);
                p.add(v);
                FuncSymbol funcSymbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName("putint");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                i++;
                LLVM_Builder.getInstance().addInstr(callInstr);
            } else if (i + 1 < s.length() && s.charAt(i) == '\\' && s.charAt(i + 1) == 'n') {
                ArrayList<LLVM_Value> p = new ArrayList<>();
                p.add(new ConstInteger(10));
                FuncSymbol funcSymbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName("putch");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                LLVM_Builder.getInstance().addInstr(callInstr);
                i++;
            } else {
                ArrayList<LLVM_Value> p = new ArrayList<>();
                p.add(new ConstInteger((int) s.charAt(i)));
                FuncSymbol funcSymbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName("putch");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                LLVM_Builder.getInstance().addInstr(callInstr);
            }
        }
        return null;
    }
}
