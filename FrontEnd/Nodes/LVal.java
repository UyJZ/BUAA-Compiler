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
import llvm_ir.Values.Instruction.*;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;
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
            if (symbol.isGlobal() && !SymbolManager.getInstance().isGlobal()) {
                instr = new LoadInstr(new Integer32Type(), symbol.getLLVMirValue());
            } else {
                instr = new LoadInstr(new Integer32Type(), symbol.getLLVMirValue());
            }
            IRController.getInstance().addInstr(instr);
            return instr;
        } else if (symbol.getDim() == 1) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) if (n instanceof Exp) values.add(n.genLLVMir());
            if (values.size() == 0) {
                Value rootPtr = getElementRootPtr(symbol);
                assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
                GEPInstr gepInstr;
                if (symbol.getType() instanceof ArrayType) {
                    gepInstr = new GEPInstr((ArrayType) symbol.getType(), rootPtr, new Value(new Integer32Type(), "0"));
                } else if (symbol.getType() instanceof PointerType && !symbol.isParam()) {
                    gepInstr = new GEPInstr((PointerType) symbol.getType(), rootPtr, new Value(new Integer32Type(), "0"));
                } else {
                    return rootPtr;
                }
                gepInstr.setLLVMtypeForFuncParam();
                IRController.getInstance().addInstr(gepInstr);
                return gepInstr;
            } else {
                Value instr = getElementRootPtr(symbol);
                assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
                if (symbol.getType() instanceof ArrayType arrayType) {
                    GEPInstr gepInstr = new GEPInstr(arrayType, instr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr = new LoadInstr(new Integer32Type(), gepInstr);
                    IRController.getInstance().addInstr(loadInstr);
                    return loadInstr;
                } else {
                    PointerType pointerType = (PointerType) symbol.getType();
                    GEPInstr gepInstr = new GEPInstr(pointerType, instr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    LoadInstr loadInstr = new LoadInstr(new Integer32Type(), gepInstr);
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
            Value RootPtr = getElementRootPtr(symbol);
            if (values.size() == 0) {
                assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
                GEPInstr gepInstr;
                if (symbol.getType() instanceof ArrayType && !symbol.isParam()) {
                    gepInstr = new GEPInstr((ArrayType) symbol.getType(), RootPtr, new Value(new Integer32Type(), "0"));
                    gepInstr.setLLVMtypeForFuncParam();
                    IRController.getInstance().addInstr(gepInstr);
                    return gepInstr;
                } else {
                    return RootPtr;
                }
            } else if (values.size() == 1) {
                assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
                GEPInstr gepInstr;
                //两种可能，二维数组or指针数组，但是这个只可能用于函数参数，因此最后只需要统一转换成指针
                if (symbol.getType() instanceof ArrayType) {
                    gepInstr = new GEPInstr((ArrayType) symbol.getType(), RootPtr, values.get(0));
                } else {
                    gepInstr = new GEPInstr((PointerType) symbol.getType(), RootPtr, values.get(0));
                }
                IRController.getInstance().addInstr(gepInstr);
                assert (gepInstr.getType() instanceof ArrayType);
                GEPInstr gepInstr1 = new GEPInstr((ArrayType) gepInstr.getType(), gepInstr, new Value(new Integer32Type(), "0"));
                IRController.getInstance().addInstr(gepInstr1);
                gepInstr1.setLLVMtypeForFuncParam();//统一转换
                return gepInstr1;
            } else {
                assert (values.size() == 2);
                assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
                //这个同上，两种
                if (symbol.getType() instanceof ArrayType arrayType) {
                    GEPInstr gepInstr = new GEPInstr(arrayType, RootPtr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    assert (arrayType.getEleType() instanceof ArrayType);
                    GEPInstr gepInstr1 = new GEPInstr((ArrayType) (arrayType.getEleType()), gepInstr, values.get(1));
                    IRController.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr = new LoadInstr(new Integer32Type(), gepInstr1);
                    IRController.getInstance().addInstr(loadInstr);
                    return loadInstr;
                } else {
                    PointerType pointerType = (PointerType) symbol.getType();
                    GEPInstr gepInstr = new GEPInstr(pointerType, RootPtr, values.get(0));
                    IRController.getInstance().addInstr(gepInstr);
                    assert (pointerType.getElementType() instanceof ArrayType);
                    GEPInstr gepInstr1 = new GEPInstr((ArrayType) pointerType.getElementType(), gepInstr, values.get(1));
                    IRController.getInstance().addInstr(gepInstr1);
                    LoadInstr loadInstr = new LoadInstr(new Integer32Type(), gepInstr1);
                    IRController.getInstance().addInstr(loadInstr);
                    return loadInstr;
                }
            }
        }
        return null;
    }

    public Value genLLVMForAssign() {
        VarSymbol symbol = (VarSymbol) SymbolManager.getInstance().getSymbolByName(name);
        if (symbol.getDim() == 0) {
            if (symbol.isGlobal()) {
                return new Value(new Integer32Type(), "@" + symbol.getSymbolName());
            } else return new Value(new Integer32Type(), symbol.getLLVMirValue().getName());
        } else if (symbol.getDim() == 1) {
            ArrayList<Value> values = new ArrayList<>();
            for (Node n : children) {
                if (n instanceof Exp) {
                    values.add(n.genLLVMir());
                }
            }
            assert (values.size() == 1);
            Value rootPtr = getElementRootPtr(symbol);
            assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
            if (symbol.getType() instanceof ArrayType arrayType) {
                GEPInstr gepInstr = new GEPInstr(arrayType, rootPtr, values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                return gepInstr;
            } else {
                PointerType pointerType = (PointerType) symbol.getType();
                GEPInstr gepInstr = new GEPInstr(pointerType, rootPtr, values.get(0));
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
            Value rootPtr = getElementRootPtr(symbol);
            assert (values.size() == 2);
            assert (symbol.getType() instanceof ArrayType || symbol.getType() instanceof PointerType);
            if (symbol.getType() instanceof ArrayType arrayType) {
                GEPInstr gepInstr = new GEPInstr(arrayType, symbol.getLLVMirValue(), values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                assert (arrayType.getEleType() instanceof ArrayType);
                GEPInstr gepInstr1 = new GEPInstr((ArrayType) (arrayType.getEleType()), gepInstr, values.get(1));
                IRController.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            } else {
                PointerType pointerType = (PointerType) symbol.getType();
                GEPInstr gepInstr = new GEPInstr(pointerType, rootPtr, values.get(0));
                IRController.getInstance().addInstr(gepInstr);
                assert (pointerType.getElementType() instanceof ArrayType);
                GEPInstr gepInstr1 = new GEPInstr((ArrayType) pointerType.getElementType(), gepInstr, values.get(1));
                IRController.getInstance().addInstr(gepInstr1);
                return gepInstr1;
            }
        }
        return null;
    }

    public Value getElementRootPtr(VarSymbol symbol) {
        Value instr;
        if (symbol.isGlobal() && !SymbolManager.getInstance().isGlobal()) {
            instr = new Value(symbol.getType(), "@" + symbol.getSymbolName());
        } else if (symbol.isParam()) {
            instr = new LoadInstr(symbol.getType(), symbol.getLLVMirValue());
            IRController.getInstance().addInstr((LoadInstr) instr);
        } else {
            instr = new Value(symbol.getType(), symbol.getLLVMirValue().getName());
        }
        return instr;
    }
}
