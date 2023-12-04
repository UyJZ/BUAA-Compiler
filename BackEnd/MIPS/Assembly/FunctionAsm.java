package BackEnd.MIPS.Assembly;

import java.util.ArrayList;

public class FunctionAsm extends Asm {
    private ArrayList<BlockAsm> blockList;

    private LabelAsm labelAsm;

    private String name;

    public FunctionAsm(String name) {
        blockList = new ArrayList<>();
        labelAsm = new LabelAsm(name);
        this.name = name;
    }

    public void addAsm(BlockAsm asm) {
        blockList.add(asm);
    }

    public void addBasicBlock(BlockAsm block) {
        blockList.add(block);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(labelAsm).append('\n');
        for (BlockAsm blockAsm : blockList) {
            stringBuilder.append(blockAsm).append("\n");
        }
        return stringBuilder.toString();
    }

}
