package BackEnd.MIPS.Assembly;

import Ir_LLVM.LLVM_Values.BasicBlock;

import java.util.ArrayList;

public class BlockAsm {

    private LabelAsm labelAsm;
    private ArrayList<Asm> AsmList;

    private FunctionAsm father;

    public BlockAsm(FunctionAsm father, BasicBlock block) {
        this.father = father;
        this.labelAsm = block.getMIPSLabel();
        block.setBlockAsm(this);
        AsmList = new ArrayList<>();
    }

    public void addAsm(Asm asm) {
        AsmList.add(asm);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(labelAsm).append('\n');
        for (Asm asm : AsmList) {
            stringBuilder.append(asm).append("\n");
        }
        return stringBuilder.toString();
    }
}
