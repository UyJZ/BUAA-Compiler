package FrontEnd.AbsSynTreeNodes;

import FrontEnd.ErrorProcesser.ErrorType;
import FrontEnd.Lexer.Token;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.AbsSynTreeNodes.Var.Number;
import FrontEnd.SymbolTable.Symbols.Symbol;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.AbsSynTreeNodes.Exp.Exp;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import IR_LLVM.LLVM_Value;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Instr.*;

import java.util.ArrayList;
import java.util.List;

public class LVal extends SynTreeNode {

    private final String name;

    public LVal(SyntaxVarType type, ArrayList<SynTreeNode> children) {
        super(type, children);
        name = ((TokenSynTreeNode) children.get(0)).getIdentName();
    }

    public int identLine() {
        return children.get(0).getEndLine();
    }

    public String getName() {
        return name;
    }

    @Override
    public int getDim() {
        int dim = SymbolTableBuilder.getInstance().getDimByName(name);
        for (SynTreeNode child : children) if (child instanceof Exp || child instanceof Number) dim--;
        return dim;
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenSynTreeNode && ((TokenSynTreeNode) children.get(0)).getTokenType() == Token.TokenType.IDENFR)
            if (!SymbolTableBuilder.getInstance().isVarDefined(((TokenSynTreeNode) children.get(0)).getIdentName()))
                ErrorList.AddError(new Error(identLine(), ErrorType.c));
        super.checkError();
    }

    public int calc() {
        String name = ((TokenSynTreeNode) children.get(0)).getIdentName();
        Symbol symbol = SymbolTableBuilder.getInstance().getSymbolByName(name);
        if (symbol == null) return -1;
        else {
            VarSymbol varSymbol = (VarSymbol) symbol;
            List<Integer> pos = new ArrayList<>();
            for (SynTreeNode n : children) {
                if (n instanceof Exp) pos.add(((Exp) n).calc());
            }
            return varSymbol.getValue(pos);
        }
    }

    @Override
    public LLVM_Value genLLVMir() {
        VarSymbol symbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            Instr instr;
            if ((symbol.isConst() && symbol.isGlobal()) || (symbol.isCalcAble() && symbol.getValueFor0Dim() != null)) {
                int val;
                if (symbol.isConst()) val = symbol.getValue(new ArrayList<>());
                else val = symbol.getValueFor0Dim();
                return new ConstInteger(val);
            } else {
                instr = new LoadInstr(symbol.getLLVMirValue());
                LLVM_Builder.getInstance().addInstr(instr);
                return instr;
            }
        } else if (symbol.getDim() == 1) {
            ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
            for (SynTreeNode n : children) if (n instanceof Exp) LLVMValues.add(n.genLLVMir());
            if (LLVMValues.size() == 0) {
                LLVM_Value rootPtr = symbol.getLLVMirValue();
                if (symbol.isParam()) {
                    LoadInstr instr = new LoadInstr(rootPtr);
                    LLVM_Builder.getInstance().addInstr(instr);
                    return instr;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), new ConstInteger(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    return gepInstr;
                }
            } else {
                LLVM_Value instr = symbol.getLLVMirValue();
                if (symbol.isConst() && symbol.isGlobal() && LLVMValues.get(0) instanceof ConstInteger) {
                    ArrayList<Integer> v = new ArrayList<>();
                    v.add(((ConstInteger) LLVMValues.get(0)).getVal());
                    return new ConstInteger(symbol.getValue(v));
                }
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(instr);
                    LLVM_Builder.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr);
                    LLVM_Builder.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(instr, new ConstInteger(0), LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr = new LoadInstr(gepInstr);
                    LLVM_Builder.getInstance().addInstr(loadInstr);
                    return loadInstr;
                }
            }
        } else if (symbol.getDim() == 2) {
            ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
            for (SynTreeNode n : children) {
                if (n instanceof Exp) {
                    LLVMValues.add(n.genLLVMir());
                }
            }
            LLVM_Value rootPtr = symbol.getLLVMirValue();
            if (LLVMValues.size() == 0) {
                if (symbol.isParam()) {
                    LoadInstr instr = new LoadInstr(rootPtr);
                    LLVM_Builder.getInstance().addInstr(instr);
                    return instr;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), new ConstInteger(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    return gepInstr;
                }
            } else if (LLVMValues.size() == 1) {
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(rootPtr);
                    LLVM_Builder.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), new ConstInteger(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr1);
                    return gepInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), new ConstInteger(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr1);
                    return gepInstr1;
                }
            } else {
                assert (LLVMValues.size() == 2);
                if (symbol.isConst() && symbol.isGlobal() && LLVMValues.get(0) instanceof ConstInteger && LLVMValues.get(1) instanceof ConstInteger) {
                    ArrayList<Integer> v = new ArrayList<>();
                    v.add(((ConstInteger) LLVMValues.get(0)).getVal());
                    v.add(((ConstInteger) LLVMValues.get(1)).getVal());
                    return new ConstInteger(symbol.getValue(v));
                }
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(rootPtr);
                    LLVM_Builder.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), LLVMValues.get(1));
                    LLVM_Builder.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr1);
                    LLVM_Builder.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), LLVMValues.get(0));
                    LLVM_Builder.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), LLVMValues.get(1));
                    LLVM_Builder.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr1);
                    LLVM_Builder.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                }
            }
        }
        return null;
    }

    public LLVM_Value genLLVMForAssign() {
        VarSymbol symbol = (VarSymbol) SymbolTableBuilder.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            return symbol.getLLVMirValue();
        } else if (symbol.getDim() == 1) {
            ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
            for (SynTreeNode n : children) {
                if (n instanceof Exp) {
                    LLVMValues.add(n.genLLVMir());
                }
            }
            assert (LLVMValues.size() == 1);
            LLVM_Value instr = symbol.getLLVMirValue();
            if (symbol.isParam()) {
                LoadInstr loadInstr = new LoadInstr(instr);
                LLVM_Builder.getInstance().addInstr(loadInstr);
                GEPInstr gepInstr = new GEPInstr(loadInstr, LLVMValues.get(0));
                LLVM_Builder.getInstance().addInstr(gepInstr);
                return gepInstr;
            } else {
                GEPInstr gepInstr = new GEPInstr(instr, new ConstInteger(0), LLVMValues.get(0));
                LLVM_Builder.getInstance().addInstr(gepInstr);
                return gepInstr;
            }
        } else if (symbol.getDim() == 2) {
            ArrayList<LLVM_Value> LLVMValues = new ArrayList<>();
            for (SynTreeNode n : children) {
                if (n instanceof Exp) {
                    LLVMValues.add(n.genLLVMir());
                }
            }
            LLVM_Value rootPtr = symbol.getLLVMirValue();
            if (symbol.isParam()) {
                LoadInstr loadInstr = new LoadInstr(rootPtr);
                LLVM_Builder.getInstance().addInstr(loadInstr);
                GEPInstr gepInstr = new GEPInstr(loadInstr, LLVMValues.get(0));
                LLVM_Builder.getInstance().addInstr(gepInstr);
                GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), LLVMValues.get(1));
                LLVM_Builder.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            } else {
                GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), LLVMValues.get(0));
                LLVM_Builder.getInstance().addInstr(gepInstr);
                GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), LLVMValues.get(1));
                LLVM_Builder.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            }
        }
        return null;
    }
}
