package FrontEnd.Nodes.Stmt;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.FuncSymbol;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.BinaryInstr;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;

public class PrintfStmt extends Stmt {

    private TokenNode formatStringNode;

    private int OutputNums = 0;

    public PrintfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        for (Node n : children)
            if (n instanceof TokenNode && ((TokenNode) n).isFormatString())
                formatStringNode = (TokenNode) n;
            else if (n instanceof Exp) OutputNums++;
    }

    @Override
    public void checkError() {
        if (OutputNums != formatStringNode.getParameterNum())
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.l));
        super.checkError();
    }

    @Override
    public Value genLLVMir() {
        ArrayList<Value> instrs = new ArrayList<>();
        for (Node n : children) {
            if (n instanceof Exp) instrs.add(n.genLLVMir());
        }
        String s = formatStringNode.getValue();
        s = s.substring(1, s.length() - 1);
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == '%' && s.charAt(i + 1) == 'd') {
                ArrayList<Value> p = new ArrayList<>();
                Value v = instrs.get(start++);
                p.add(v);
                FuncSymbol funcSymbol = SymbolManager.getInstance().getFuncSymbolByFuncName("putint");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                i++;
                IRController.getInstance().addInstr(callInstr);
            } else if (i + 1 < s.length() && s.charAt(i) == '\\' && s.charAt(i + 1) == 'n') {
                ArrayList<Value> p = new ArrayList<>();
                p.add(new ConstInteger(10));
                FuncSymbol funcSymbol = SymbolManager.getInstance().getFuncSymbolByFuncName("putch");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                IRController.getInstance().addInstr(callInstr);
                i++;
            } else {
                ArrayList<Value> p = new ArrayList<>();
                p.add(new ConstInteger((int) s.charAt(i)));
                FuncSymbol funcSymbol = SymbolManager.getInstance().getFuncSymbolByFuncName("putch");
                CallInstr callInstr = new CallInstr(new VoidType(), funcSymbol.getLLVMirValue(), p);
                IRController.getInstance().addInstr(callInstr);
            }
        }
        return null;
    }
}
