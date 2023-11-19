package BackEnd.MIPS.Assembly;

import java.util.ArrayList;

public class FunctionAsm {
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

}
