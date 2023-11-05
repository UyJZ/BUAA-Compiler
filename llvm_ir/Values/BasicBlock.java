package llvm_ir.Values;

import llvm_ir.Value;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock extends Value {
    private Value label;

    private Instr terminator;
    public BasicBlock(LLVMType type, String name) {
        super(type, name);
        instrs = new ArrayList<>();
    }

    private int varCnt = 0;
    protected ArrayList<Instr> instrs;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Instr instr : instrs) {
            sb.append("    ").append(instr.toString()).append("\n");
        }
        return sb.toString();
    }

    public void addInstr(Instr instr) {
        instrs.add(instr);
    }

}
