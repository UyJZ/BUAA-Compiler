package FrontEnd.Nodes;

import Enums.ErrorType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Nodes.Exp.Exp;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Instruction.*;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.PointerType;

import java.util.ArrayList;
import java.util.List;

public class LVal extends Node {

    private final String name;

    public LVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
    }

    public int identLine() {
        return children.get(0).getEndLine();
    }

    public String getName() {
        return name;
    }

    @Override
    public int getDim() {
        int dim = SymbolManager.getInstance().getDimByName(name);
        for (Node child : children) if (child instanceof Exp) dim--;
        return dim;
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenNode && ((TokenNode) children.get(0)).getTokenType() == tokenType.IDENFR)
            if (!SymbolManager.getInstance().isVarDefined(((TokenNode) children.get(0)).getIdentName()))
                ErrorChecker.AddError(new Error(identLine(), ErrorType.c));
        super.checkError();
    }

    public int calc() {
        String name = ((TokenNode) children.get(0)).getIdentName();
        Symbol symbol = SymbolManager.getInstance().getSymbolByName(name);
        if (symbol == null) return -1;
        else {
            VarSymbol varSymbol = (VarSymbol) symbol;
            List<Integer> pos = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) pos.add(((Exp) n).calc());
            }
            return varSymbol.getValue(pos);
        }
    }

    @Override
    public Value genLLVMir() {
        VarSymbol symbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            Instr instr;
            if ((symbol.isConst() && symbol.isGlobal()) || (symbol.isCalcAble() && symbol.getValueFor0Dim() != null)) {
                int val;
                if (symbol.isConst()) val = symbol.getValue(new ArrayList<>());
                else val = symbol.getValueFor0Dim();
                return new ConstInteger(val);
            } else {
                instr = new LoadInstr(symbol.getLLVMirValue());
                IRController.getInstance().addInstr(instr);
                return instr;
            }
        } else if (symbol.getDim() == 1) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) if (n instanceof Exp) values.add(n.genLLVMir());
            if (values.size() == 0) {
                Value rootPtr = symbol.getLLVMirValue();
                /*
                if (symbol.isParam()) {
                    LoadInstr instr = new LoadInstr(rootPtr);
                    IRController.getInstance().addInstr(instr);
                    return instr;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), new ConstInteger(0));
                    IRController.getInstance().addInstr(gepInstr);
                    return gepInstr;
                }
                 */
                if (symbol.isParam()) {
                    LoadInstr instr = new LoadInstr(rootPtr);
                    IRController.getInstance().addInstr(instr);
                    return instr;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), new ConstInteger(0));
                    IRController.getInstance().addInstr(gepInstr);
                    return gepInstr;
                }
            } else {
                Value instr = symbol.getLLVMirValue();
                if (symbol.isConst() && symbol.isGlobal() && values.get(0) instanceof ConstInteger) {
                    ArrayList<Integer> v = new ArrayList<>();
                    v.add(((ConstInteger) values.get(0)).getVal());
                    return new ConstInteger(symbol.getValue(v));
                }
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(instr);
                    IRController.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr);
                    IRController.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(instr, new ConstInteger(0), values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr = new LoadInstr(gepInstr);
                    IRController.getInstance().addInstr(loadInstr);
                    return loadInstr;
                }
            }
        } else if (symbol.getDim() == 2) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) {
                    values.add(n.genLLVMir());
                }
            }
            Value rootPtr = symbol.getLLVMirValue();
            if (values.size() == 0) {
                if (symbol.isParam()) {
                    LoadInstr instr = new LoadInstr(rootPtr);
                    IRController.getInstance().addInstr(instr);
                    return instr;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), new ConstInteger(0));
                    IRController.getInstance().addInstr(gepInstr);
                    return gepInstr;
                }
            } else if (values.size() == 1) {
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(rootPtr);
                    IRController.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), new ConstInteger(0));
                    IRController.getInstance().addInstr(gepInstr1);
                    return gepInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), new ConstInteger(0));
                    IRController.getInstance().addInstr(gepInstr1);
                    return gepInstr1;
                }
            } else {
                assert (values.size() == 2);
                if (symbol.isConst() && symbol.isGlobal() && values.get(0) instanceof ConstInteger && values.get(1) instanceof ConstInteger) {
                    ArrayList<Integer> v = new ArrayList<>();
                    v.add(((ConstInteger) values.get(0)).getVal());
                    v.add(((ConstInteger) values.get(1)).getVal());
                    return new ConstInteger(symbol.getValue(v));
                }
                if (symbol.isParam()) {
                    LoadInstr loadInstr = new LoadInstr(rootPtr);
                    IRController.getInstance().addInstr(loadInstr);
                    GEPInstr gepInstr = new GEPInstr(loadInstr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), values.get(1));
                    IRController.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr1);
                    IRController.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                } else {
                    GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), values.get(1));
                    IRController.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr1 = new LoadInstr(gepInstr1);
                    IRController.getInstance().addInstr(loadInstr1);
                    return loadInstr1;
                }
            }
        }
        return null;
    }

    public Value genLLVMForAssign() {
        VarSymbol symbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            return symbol.getLLVMirValue();
        } else if (symbol.getDim() == 1) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) {
                    values.add(n.genLLVMir());
                }
            }
            assert (values.size() == 1);
            Value instr = symbol.getLLVMirValue();
            if (symbol.isParam()) {
                LoadInstr loadInstr = new LoadInstr(instr);
                IRController.getInstance().addInstr(loadInstr);
                GEPInstr gepInstr = new GEPInstr(loadInstr, values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                return gepInstr;
            } else {
                GEPInstr gepInstr = new GEPInstr(instr, new ConstInteger(0), values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                return gepInstr;
            }
        } else if (symbol.getDim() == 2) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) {
                    values.add(n.genLLVMir());
                }
            }
            Value rootPtr = symbol.getLLVMirValue();
            if (symbol.isParam()) {
                LoadInstr loadInstr = new LoadInstr(rootPtr);
                IRController.getInstance().addInstr(loadInstr);
                GEPInstr gepInstr = new GEPInstr(loadInstr, values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), values.get(1));
                IRController.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            } else {
                GEPInstr gepInstr = new GEPInstr(rootPtr, new ConstInteger(0), values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), values.get(1));
                IRController.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            }
        }
        return null;
    }
}
