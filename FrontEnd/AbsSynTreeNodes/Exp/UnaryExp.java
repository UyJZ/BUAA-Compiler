package FrontEnd.AbsSynTreeNodes.Exp;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.Func.FuncRParams;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import FrontEnd.AbsSynTreeNodes.UnaryOp;
import FrontEnd.SymbolTable.Symbols.FuncSymbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstBool;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Instr.BinaryInstr;
import Ir_LLVM.LLVM_Values.Instr.CallInstr;
import Ir_LLVM.LLVM_Values.Instr.IcmpInstr;
import Ir_LLVM.LLVM_Types.Integer32Type;
import Ir_LLVM.LLVM_Types.VoidType;

import java.util.ArrayList;
import java.util.Objects;

public class UnaryExp extends Node {
    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        int start = 0;
    }

    @Override
    public void checkError() {
        super.checkError();
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getIdentName() != null) {
            if (!SymbolTableBuilder.getInstance().isVarFuncDefined(((TokenNode) children.get(0)).getIdentName())) {
                ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.c));
                return;
            }
            ArrayList<Integer> dims = new ArrayList<>();
            if (children.size() > 2 && children.get(2) instanceof FuncRParams)
                dims = ((FuncRParams) children.get(2)).getDims();
            FuncSymbol symbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName(((TokenNode) children.get(0)).getIdentName());
            if (symbol.getDimList().size() != dims.size()) {
                ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.d));
            } else for (int i = 0; i < dims.size(); i++) {
                if (!Objects.equals((symbol).getDimList().get(i), dims.get(i))) {
                    ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.e));
                }
            }
        }
    }


    @Override
    public int getDim() {
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getTokenType() == tokenType.IDENFR) {
            return SymbolTableBuilder.getInstance().getDimByName(((TokenNode) children.get(0)).getIdentName());
        }
        for (Node n : children) if (n.getDim() != -1) return n.getDim();
        return -1;
    }

    public int calc() {
        if (children.size() == 1 && children.get(0) instanceof PrimaryExp) return ((PrimaryExp) children.get(0)).calc();
        else if (children.size() == 2) {
            if (((UnaryOp) children.get(0)).getOp() == tokenType.MINU) {
                return -((UnaryExp) children.get(1)).calc();
            } else {
                return ((UnaryExp) children.get(1)).calc();
            }
        } else return -1;
    }

    @Override
    public LLVM_Value genLLVMir() {
        if (children.size() == 1 && children.get(0) instanceof PrimaryExp) return children.get(0).genLLVMir();
        else if (children.size() == 2) {
            if (((UnaryOp) children.get(0)).getOp() == tokenType.MINU) {
                LLVM_Value operand1 = children.get(1).genLLVMir();
                if (operand1 instanceof ConstInteger constInteger) {
                    return new ConstInteger(-constInteger.getVal());
                }
                BinaryInstr binaryInstr = new BinaryInstr(new Integer32Type(), operand1, BinaryInstr.op.SUB);
                LLVM_Builder.getInstance().addInstr(binaryInstr);
                return binaryInstr;
            } else if ((((UnaryOp) children.get(0)).getOp()) == tokenType.NOT) {
                LLVM_Value operand1 = children.get(1).genLLVMir();
                if (operand1 instanceof ConstInteger constInteger) {
                    if (constInteger.getVal() != 0) return new ConstBool(false);
                    else return new ConstBool(true);
                }
                IcmpInstr instr = new IcmpInstr(operand1, new ConstInteger(0), IcmpInstr.CmpOp.eq);
                LLVM_Builder.getInstance().addInstr(instr);
                return instr;
            } else
                return children.get(1).genLLVMir();
        } else {
            FuncSymbol funcSymbol = SymbolTableBuilder.getInstance().getFuncSymbolByFuncName(((TokenNode) children.get(0)).getIdentName());
            ArrayList<LLVM_Value> params = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof FuncRParams) {
                    params.addAll(((FuncRParams) n).genLLVMirForFunc());
                }
            }
            CallInstr callInstr;
            if (funcSymbol.getLLVMType() instanceof VoidType)
                callInstr = new CallInstr(funcSymbol.getLLVMType(), funcSymbol.getLLVMirValue(), params);
            else
                callInstr = new CallInstr(funcSymbol.getLLVMType(), funcSymbol.getLLVMirValue(), params);
            LLVM_Builder.getInstance().addInstr(callInstr);
            return callInstr;
        }
    }
}
