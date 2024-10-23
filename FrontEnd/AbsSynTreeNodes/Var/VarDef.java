package FrontEnd.AbsSynTreeNodes.Var;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorProcesser.Error;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.ErrorProcesser.ErrorExceptions.RenameException;
import FrontEnd.AbsSynTreeNodes.Exp.ConstExp;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.AbsSynTreeNodes.TokenNode;
import Ir_LLVM.InitializedValue;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import FrontEnd.SymbolTable.Symbols.VarSymbol;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.GlobalVar;
import Ir_LLVM.LLVM_Values.Instr.AllocaInst;
import Ir_LLVM.LLVM_Values.Instr.GEPInstr;
import Ir_LLVM.LLVM_Values.Instr.StoreInstr;
import Ir_LLVM.LLVM_Types.ArrayType;
import Ir_LLVM.LLVM_Types.Integer32Type;
import Ir_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;

public class VarDef extends Node {

    private int dim = 0;
    private final String name;

    private boolean isAssigned;

    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        name = ((TokenNode) children.get(0)).getIdentName();
        isAssigned = false;
        for (Node child : children) {
            if (child instanceof TokenNode && ((TokenNode) child).getTokenType() == tokenType.ASSIGN) isAssigned = true;
            if (child instanceof ConstExp) {
                dim++;
            }
        }
    }

    private VarSymbol createSymbol() {
        ArrayList<Integer> lens = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof ConstExp) {
                lens.add(((ConstExp) child).calc());
            }
        }
        InitializedValue initValue = null;
        for (Node child : children) {
            if (child instanceof InitVal && SymbolTableBuilder.getInstance().isGlobal()) {
                initValue = ((InitVal) child).getVal();
            }
        }
        VarSymbol symbol = new VarSymbol(name, SymbolType.SYMBOL_VAR, dim, false, lens);
        if (initValue != null) {
            symbol.setInitValue(initValue);
        }
        return symbol;
    }

    @Override
    public void checkError() {
        try {
            SymbolTableBuilder.getInstance().addSymbol(new VarSymbol(name, SymbolType.SYMBOL_VAR, dim, false));
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        super.checkError();
    }

    @Override
    public LLVM_Value genLLVMir() {
        VarSymbol symbol = createSymbol();
        if (SymbolTableBuilder.getInstance().isGlobal()) {
            GlobalVar globalVar = new GlobalVar(symbol);
            LLVM_Builder.getInstance().addGlobalVar(globalVar);
            symbol.setLlvmValue(globalVar);
        } else {
            LLVMType varType = new LLVMType();
            if (symbol.getDim() == 0)
                varType = new Integer32Type();
            else
                varType = new ArrayType(symbol.getLens(), new Integer32Type());
            AllocaInst allocaInst = new AllocaInst(varType);
            LLVM_Builder.getInstance().addInstr(allocaInst);
            symbol.setLlvmValue(allocaInst);
            if (symbol.getDim() == 1) {
                if (isAssigned) {
                    ArrayList<LLVM_Value> LLVMValues = ((InitVal) children.get(children.size() - 1)).genLLVMirListFor1Dim();
                    //GEP and store
                    for (int i = 0; i < LLVMValues.size(); i++) {
                        ArrayType type1 = new ArrayType(symbol.getLens(), new Integer32Type());
                        GEPInstr gepInstr = new GEPInstr(symbol.getLLVMirValue(), new ConstInteger(0), new ConstInteger(i));
                        LLVM_Builder.getInstance().addInstr(gepInstr);
                        StoreInstr storeInstr = new StoreInstr(LLVMValues.get(i), gepInstr);
                        LLVM_Builder.getInstance().addInstr(storeInstr);
                    }
                }
            } else if (symbol.getDim() == 2) {
                if (isAssigned) {
                    ArrayList<ArrayList<LLVM_Value>> v = ((InitVal) children.get(children.size() - 1)).genLLVMirListFor2Dim();
                    for (int i = 0; i < v.size(); i++) {
                        ArrayType type1 = new ArrayType(symbol.getLens(), new Integer32Type());
                        LLVMType type2 = type1.getElementType();
                        GEPInstr gepInstr = new GEPInstr(symbol.getLLVMirValue(), new ConstInteger(0), new ConstInteger(i));
                        LLVM_Builder.getInstance().addInstr(gepInstr);
                        for (int j = 0; j < v.get(i).size(); j++) {
                            //两个gep
                            assert (type2 instanceof ArrayType);
                            GEPInstr gepInstr1 = new GEPInstr(gepInstr, new ConstInteger(0), new ConstInteger(j));
                            LLVM_Builder.getInstance().addInstr(gepInstr1);
                            StoreInstr storeInstr = new StoreInstr(v.get(i).get(j), gepInstr1);
                            LLVM_Builder.getInstance().addInstr(storeInstr);
                        }
                    }
                }
                //GEP and store
            } else {
                assert (symbol.getDim() == 0);
                if (isAssigned) {
                    LLVM_Value LLVMValue = ((InitVal) children.get(children.size() - 1)).genLLVMir();
                    if (LLVMValue instanceof ConstInteger constInteger) {
                        ArrayList<ArrayList<Integer>> l = new ArrayList<>();
                        l.add(new ArrayList<>());
                        l.get(0).add(constInteger.getVal());
                        symbol.setInitValue(new InitializedValue(0, l));
                        StoreInstr storeInstr = new StoreInstr(constInteger, allocaInst);
                        LLVM_Builder.getInstance().addInstr(storeInstr);
                    } else {
                        StoreInstr storeInstr = new StoreInstr(LLVMValue, allocaInst);
                        LLVM_Builder.getInstance().addInstr(storeInstr);
                    }
                }
            }
        }
        try {
            SymbolTableBuilder.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorList.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        return null;
    }
}
