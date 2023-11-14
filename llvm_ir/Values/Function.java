package llvm_ir.Values;

import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;

public class Function extends Value {

    private ArrayList<Param> paramArrayList;

    private boolean hasParam;

    public Function(LLVMType type, String name, boolean hasParam) {
        super(type, "@" + name);
        paramArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
        this.hasParam = hasParam;
    }

    private ArrayList<BasicBlock> blockArrayList;

    public void addBasicBlock(BasicBlock basicBlock) {
        if (blockArrayList.size() == 0) basicBlock.setFirstBlock();
        blockArrayList.add(basicBlock);
    }

    public ArrayList<BasicBlock> getBlockArrayList() {
        return blockArrayList;
    }

    public void addParam(Param param) {
        paramArrayList.add(param);
    }

    public ArrayList<Param> getParamArrayList() {
        return paramArrayList;
    }

    public boolean hasParam() {
        return hasParam;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ").append(type.toString()).append(" ").append(name);
        sb.append("(");
        for (Param param : paramArrayList) {
            sb.append(param.toString()).append(", ");
        }
        if (paramArrayList.size() > 0) sb.delete(sb.length() - 2, sb.length());
        sb.append(") {\n");
        for (BasicBlock basicBlock : blockArrayList) {
            sb.append(basicBlock.toString());
        }
        sb.append("}\n");
        return sb.toString();
    }

    public boolean isLastInstrReturnVoid() {
        Instr instr = blockArrayList.get(blockArrayList.size() - 1).lastInstr();
        return instr instanceof ReturnInstr && instr.getType() instanceof VoidType;
    }

    public String getName() {
        return name;
    }
}