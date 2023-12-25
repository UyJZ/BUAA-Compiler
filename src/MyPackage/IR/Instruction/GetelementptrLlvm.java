package MyPackage.IR.Instruction;

import MyPackage.IR.DeclLlvm;
import MyPackage.IR.Type;
import MyPackage.IR.User;
import MyPackage.IR.Value;
import MyPackage.MIPS.Instruction.Add;
import MyPackage.MIPS.Instruction.Addi;
import MyPackage.MIPS.Instruction.La;
import MyPackage.MIPS.Instruction.Li;
import MyPackage.MIPS.Instruction.Mflo;
import MyPackage.MIPS.Instruction.Mult;
import MyPackage.MIPS.Instruction.Sll;
import MyPackage.MIPS.Instruction.Sw;
import MyPackage.MIPS.MipsModule;
import MyPackage.OutPut;
import MyPackage.Symbol.MyValSymbol;

public class GetelementptrLlvm extends User {
    MyValSymbol symbol;

    public GetelementptrLlvm(Type type, int value, MyValSymbol symbol) {
        super(type, value);
        this.symbol = symbol;
    }

    @Override
    public void print() {
        OutPut.printLlvm(String.format("    %s = getelementptr %s ,%s* "
                , super.printValue(), getOperands().get(0).printType(), getOperands().get(0).printType()));
        if (symbol.isGlobal()) {
            OutPut.printLlvm(String.format("@%s", symbol.getName()));
        } else {
            OutPut.printLlvm(String.format("%s", getOperands().get(0).printValue()));
        }
        for (int i = 1; i < getOperands().size(); i++) {
            OutPut.printLlvm(String.format(", %s %s", getOperands().get(i).printType(), getOperands().get(i).printValue()));
        }
        OutPut.printLlvm("\n");
    }

    @Override
    public String printType() {
        if (!getType().equals(Type.Array)) {
            return super.printType();
        } else {
            int dim;
            if (symbol.isGlobal()) {
                dim = ((DeclLlvm) symbol.getReg()).getDim().get(1);
            } else {
                dim = ((AllocaLlvm) symbol.getReg()).getArrayLlvm().getDim().get(((AllocaLlvm) symbol.getReg()).getArrayLlvm().getDim().size() - 1);
            }
            return String.format("[%d x i32]*", dim);


        }

    }

    public void generateMips() {
        int level = symbol.getLevel();
        String op1 = MipsModule.getReg();
        String op = MipsModule.getReg();
        if (symbol.isGlobal()) {
            MipsModule.addText(new La(op1, symbol.getName()));
        }
        else {
            if (MipsModule.alloc.containsKey(getOperands().get(0).getValue())) {
                int off = MipsModule.getAddress(getOperands().get(0).getValue());
                MipsModule.addText(new Addi(op1, "$sp", off));
            }
            else {
                String op2 = MipsModule.searchReg3(getOperands().get(0).getValue(), op);
                MipsModule.addText(new Add(op1, op2, "$0"));
            }
        }
        if (level == 1 || getOperands().size() < 3) {
            int l = getOperands().size();
            Value value = getOperands().get(l-1);
            if (value.getType().equals(Type.MyConst)) {
                MipsModule.addText(new Addi(op1, op1, 4*value.getValue()));
            }
            else {
                String op2 = MipsModule.searchReg3(value.getValue(), op);
                MipsModule.addText(new Add("$at", op2, "$0"));
                MipsModule.addText(new Sll("$at", "$at", 2));
                MipsModule.addText(new Add(op1, op1, "$at"));
            }
        }
        else {
            int l = getOperands().size();
            int dim;
            if (getOperands().get(0) instanceof LoadLlvm) {
                dim = ((LoadLlvm)getOperands().get(0)).getArrayLlvm().getDim().get(0);
            }
            else if (getOperands().get(0) instanceof AllocaLlvm) {
                dim = ((AllocaLlvm)getOperands().get(0)).getArrayLlvm().getDim().get(1);
            }
            else {
                dim = ((DeclLlvm)getOperands().get(0)).getDim().get(1);
            }
            Value value1 = getOperands().get(l-2);
            Value value2 = getOperands().get(l-1);
            boolean flag = false;
            if (value1.getType().equals(Type.MyConst)) {
                MipsModule.addText(new Addi(op1, op1, 4*value1.getValue()*dim));
            }
            else {
                String op2 = MipsModule.searchReg3(value1.getValue(), op);
                MipsModule.addText(new Li("$at", dim));
                MipsModule.addText(new Mult("$at", op2));
                MipsModule.addText(new Mflo("$at"));
                flag = true;
            }
            if (value2.getType().equals(Type.MyConst)) {
                MipsModule.addText(new Addi(op1, op1, 4*value2.getValue()));
            }
            else {
                String op2 = MipsModule.searchReg3(value2.getValue(), op);
                if (flag) {
                    MipsModule.addText(new Add("$at", op2, "$at"));
                }
                else {
                    MipsModule.addText(new Add("$at", op2, "$0"));
                }
                flag = true;
            }
            if (flag) {
                MipsModule.addText(new Sll("$at", "$at", 2));
                MipsModule.addText(new Add(op1, op1, "$at"));
            }
        }
        MipsModule.addReg(getValue(), op1);
    }

}
