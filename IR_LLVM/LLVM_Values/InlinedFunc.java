package IR_LLVM.LLVM_Values;

import IR_LLVM.LLVM_Values.Instr.PhiInstr;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;

public class InlinedFunc {

    private ArrayList<BasicBlock> blocks;

    private LLVMType type;

    private PhiInstr phi;

    public InlinedFunc(LLVMType type, ArrayList<BasicBlock> blocks, PhiInstr phi) {
        this.type = type;
        this.blocks = new ArrayList<>(blocks);
        this.phi = phi;
    }

    public ArrayList<BasicBlock> getBlocks() {
        return blocks;
    }

    public LLVMType getType() {
        return type;
    }

    public PhiInstr getPhi() {
        return phi;
    }

}
