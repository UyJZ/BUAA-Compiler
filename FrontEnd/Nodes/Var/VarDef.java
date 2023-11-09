package FrontEnd.Nodes.Var;

import Enums.ErrorType;
import Enums.SymbolType;
import Enums.SyntaxVarType;
import Enums.tokenType;
import FrontEnd.ErrorManager.Error;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.ErrorManager.RenameException;
import FrontEnd.Nodes.Exp.ConstExp;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;
import FrontEnd.Symbol.Initial;
import FrontEnd.Symbol.Symbol;
import FrontEnd.Symbol.SymbolManager;
import FrontEnd.Symbol.VarSymbol;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.GlobalVar;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.GEPInstr;
import llvm_ir.Values.Instruction.StoreInstr;
import llvm_ir.llvmType.ArrayType;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.PointerType;

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
        Initial initValue = null;
        for (Node child : children) {
            if (child instanceof InitVal && SymbolManager.getInstance().isGlobal()) {
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
        Symbol symbol = createSymbol();
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
    }

    @Override
    public Value genLLVMir() {
        VarSymbol symbol = createSymbol();
        if (SymbolManager.getInstance().isGlobal()) {
            GlobalVar globalVar = new GlobalVar(symbol);
            IRController.getInstance().addGlobalVar(globalVar);
        } else {
            LLVMType varType = new LLVMType();
            if (symbol.getDim() == 0)
                varType = new Integer32Type();
            else
                varType = new ArrayType(symbol.getLens(), new Integer32Type());
            AllocaInst allocaInst = new AllocaInst(varType, IRController.getInstance().genVirtualRegNum());
            IRController.getInstance().addInstr(allocaInst);
            symbol.setLlvmValue(allocaInst);
            if (symbol.getDim() == 1) {
                if (isAssigned) {
                    ArrayList<Value> values = ((InitVal) children.get(children.size() - 1)).genLLVMirListFor1Dim();
                    //GEP and store
                    for (int i = 0; i < values.size(); i++) {
                        ArrayType type1 = new ArrayType(symbol.getLens(), new Integer32Type());
                        GEPInstr gepInstr = new GEPInstr(type1, symbol.getLLVMirValue().getName(), new Value(new Integer32Type(), String.valueOf(i)));
                        IRController.getInstance().addInstr(gepInstr);
                        StoreInstr storeInstr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), values.get(i).getName(), gepInstr.getName());
                        IRController.getInstance().addInstr(storeInstr);
                    }
                }
            } else if (symbol.getDim() == 2) {
                if (isAssigned) {
                    ArrayList<ArrayList<Value>> v = ((InitVal) children.get(children.size() - 1)).genLLVMirListFor2Dim();
                    for (int i = 0; i < v.size(); i++) {
                        ArrayType type1 = new ArrayType(symbol.getLens(), new Integer32Type());
                        LLVMType type2 = type1.getEleType();
                        GEPInstr gepInstr = new GEPInstr(type1, symbol.getLLVMirValue().getName(), new Value(new Integer32Type(), String.valueOf(i)));
                        IRController.getInstance().addInstr(gepInstr);
                        for (int j = 0; j < v.get(i).size(); j++) {
                            //两个gep
                            assert (type2 instanceof ArrayType);
                            GEPInstr gepInstr1 = new GEPInstr((ArrayType) type2, gepInstr.getName(), new Value(new Integer32Type(), String.valueOf(j)));
                            IRController.getInstance().addInstr(gepInstr1);
                            StoreInstr storeInstr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), v.get(i).get(j).getName(), gepInstr1.getName());
                            IRController.getInstance().addInstr(storeInstr);
                        }
                    }
                }
                //GEP and store
            } else {
                assert (symbol.getDim() == 0);
                if (isAssigned) {
                    StoreInstr storeInstr = new StoreInstr(new Integer32Type(), new PointerType(new Integer32Type()), ((InitVal) children.get(children.size() - 1)).genLLVMir().getName(), allocaInst.getName());
                    IRController.getInstance().addInstr(storeInstr);
                }
            }
        }
        try {
            SymbolManager.getInstance().addSymbol(symbol);
        } catch (RenameException e) {
            ErrorChecker.AddError(new Error(children.get(0).getEndLine(), ErrorType.b));
        }
        return null;
    }
}
