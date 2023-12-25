package MyPackage.Parse;

import MyPackage.IR.ArrayLlvm;
import MyPackage.IR.IRModule;
import MyPackage.IR.Instruction.AllocaLlvm;
import MyPackage.IR.Instruction.RetLlvm;
import MyPackage.IR.Instruction.StoreLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.Use;
import MyPackage.IR.User;
import MyPackage.IR.Value;
import MyPackage.Symbol.MyValSymbol;

import java.util.ArrayList;

public class Block {
    private ArrayList<BlockItem> blockItems;

    public Block(ArrayList<BlockItem> blockItems) {
        this.blockItems = blockItems;
    }

    public void generateLlvm(boolean isFunction) {
        IRModule.setCurTable(IRModule.getCurTable().getNextTable());
        if (isFunction) {
            ArrayList<Value> values = IRModule.curFunction.getParams();
            ArrayList<String> names = IRModule.curFunction.getParamsName();
            for (int i = 0; i < values.size(); i++) {
                int id = IRModule.getRegID();
                AllocaLlvm allocaLlvm = new AllocaLlvm(values.get(i).getType(), id);
                MyValSymbol symbol = (MyValSymbol) IRModule.getCurTable().search(names.get(i));
                symbol.setReg(allocaLlvm);
                IRModule.curFunction.getCurrentBlock().addInstruction(allocaLlvm);
                StoreLlvm storeLlvm = new StoreLlvm(values.get(i).getType(), values.get(i).getValue(), symbol);
                storeLlvm.addOperand(values.get(i));
                storeLlvm.addOperand(allocaLlvm);
                if (values.get(i) instanceof ArrayLlvm) {
                    allocaLlvm.setArrayLlvm((ArrayLlvm) values.get(i));
                }
                IRModule.curFunction.getCurrentBlock().addInstruction(storeLlvm);
            }
        }
        for (int i = 0; i < blockItems.size(); i++) {
            blockItems.get(i).generateLlvm();
        }
        if (isFunction && (blockItems.size() == 0 || blockItems.get(blockItems.size() - 1).getStmt() == null ||
                blockItems.get(blockItems.size() - 1).getStmt().getType() != 8 &&
                        blockItems.get(blockItems.size() - 1).getStmt().getType() != 11))
        {
            IRModule.curFunction.getCurrentBlock().addInstruction(new RetLlvm(Type.Void, 0));
        }
        IRModule.setCurTable(IRModule.getCurTable().getPre());
    }
}
