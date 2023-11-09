package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicBlock extends Value {
    private Value label;

    private Instr terminator;

    private ArrayList<BasicBlock> preBlocks = new ArrayList<>();

    private boolean isFirstBlock;

    public BasicBlock() {
        super(new BasicBlockType(), "");
        instrs = new ArrayList<>();
        this.isFirstBlock = false;
    }


    public void addPreBlock(BasicBlock preBlock) {
        preBlocks.add(preBlock);
    }

    private int varCnt = 0;
    protected ArrayList<Instr> instrs;

    public void setFirstBlock() {
        this.isFirstBlock = true;
    }

    @Override
    public String toString() {
        if (instrs.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        if (!isFirstBlock) sb.append(name.substring(1)).append(":\n");
        for (Instr instr : instrs) {
            sb.append("    ").append(instr.toString()).append("\n");
        }
        return sb.toString();
    }

    public void addInstr(Instr instr) {
        instrs.add(instr);
    }


    public void setName(String name) {
        this.name = name;
    }

    public Instr lastInstr() {
        if (instrs.size() == 0) return null;
        else return instrs.get(instrs.size() - 1);
    }

}
