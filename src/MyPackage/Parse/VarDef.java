package MyPackage.Parse;

import MyPackage.IR.DeclLlvm;
import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.AllocaLlvm;
import MyPackage.IR.Instruction.GetelementptrLlvm;
import MyPackage.IR.Instruction.LoadLlvm;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;
import MyPackage.Symbol.Symbol;

import java.util.ArrayList;

public class VarDef {
    private String ident;
    private ArrayList<ConstExp> constExps;
    private InitVal initVal;

    public VarDef(ArrayList<ConstExp> constExps, InitVal initVal, String ident) {
        this.constExps = constExps;
        this.initVal = initVal;
        this.ident = ident;
    }

    public void generateLlvm(boolean isGlobal) {
        if (isGlobal) {
            Symbol symbol = IRModule.getRoot().search(ident);
            DeclLlvm declLlvm = new DeclLlvm(Type.MyConst, 0, symbol.getName(), false);
            if (initVal != null) {
                initVal.generateLlvm(declLlvm.getVal());
            }
            for (int i = 0; i < constExps.size(); i++) {
                declLlvm.addDim(constExps.get(i).getValue());
            }
            ((MyValSymbol)symbol).setReg(declLlvm);
            ((MyValSymbol) symbol).setGlobal(true);
            IRModule.addDecls(declLlvm);
        }
        else {
            Symbol symbol = IRModule.getCurTable().search(ident);
            int id = IRModule.getRegID();
            AllocaLlvm allocaLlvm;
            if (constExps.size() == 0) {
                allocaLlvm = new AllocaLlvm(Type.Reg, id);
            }
            else {
                allocaLlvm = new AllocaLlvm(Type.Array, id);
                for (int i = 0; i < constExps.size(); i++) {
                    allocaLlvm.getArrayLlvm().addDim(constExps.get(i).getValue());
                }
            }
            ((MyValSymbol) symbol).setReg(allocaLlvm);
            IRModule.curFunction.getCurrentBlock().addInstruction(allocaLlvm);
            if (initVal != null) {
                ArrayList<Value> values = new ArrayList<>();
                initVal.generateLlvm2(values);
                if (allocaLlvm.getType().equals(Type.Reg)) {
                    Value value = values.get(0);
                    StoreLlvm storeLlvm = new StoreLlvm(value.getType(), value.getValue(), (MyValSymbol)symbol);
                    storeLlvm.addOperand(value);
                    storeLlvm.addOperand(((MyValSymbol) symbol).getReg());
                    IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
                }
                else {
                    for (int i = 0; i < values.size(); i++) {
                        Value value = values.get(i);
                        int regId = IRModule.getRegID();
                        ArrayList<Integer> dim = allocaLlvm.getArrayLlvm().getDim();
                        GetelementptrLlvm getelementptrLlvm = new GetelementptrLlvm(Type.Reg, regId, (MyValSymbol) symbol);
                        getelementptrLlvm.addOperand(allocaLlvm);
                        getelementptrLlvm.addOperand(new Value(Type.MyConst, 0));
                        if (dim.size() == 1) {
                            getelementptrLlvm.addOperand(new Value(Type.MyConst, i));
                        }
                        else {
                            getelementptrLlvm.addOperand(new Value(Type.MyConst, i/dim.get(1)));
                            getelementptrLlvm.addOperand(new Value(Type.MyConst, i%dim.get(1)));
                        }
                        IRModule.curFunction.getCurrentBlock().addInstruction(getelementptrLlvm);
                        StoreLlvm storeLlvm = new StoreLlvm(value.getType(), value.getValue(), (MyValSymbol)symbol);
                        storeLlvm.addOperand(value);
                        storeLlvm.addOperand(getelementptrLlvm);
                        IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
                    }
                }
            }
        }
    }
}
