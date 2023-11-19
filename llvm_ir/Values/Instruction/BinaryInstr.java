package llvm_ir.Values.Instruction;

import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.LLVMType;
import Config.tasks;

public class BinaryInstr extends Instr {

    public enum op {
        ADD, SUB, SREM, MUL, SDIV, AND, OR
    }

    private op opcode;

    public BinaryInstr(LLVMType type, Value oprand1, Value oprand2, op opcode) {
        super(type, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum()); //TODO:nameGen
        this.opcode = opcode;
        this.operands.add(oprand1);
        this.operands.add(oprand2);
    }

    public BinaryInstr(LLVMType type, Value operand, op opcode) { //for -x or +x
        super(type, tasks.isOptimize ? "" : IRController.getInstance().genVirtualRegNum());
        this.opcode = opcode;
        this.operands.add(operand);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ");
        switch (opcode) {
            case ADD -> sb.append("add ");
            case SUB -> sb.append("sub ");
            case SREM -> sb.append("srem ");
            case MUL -> sb.append("mul ");
            case SDIV -> sb.append("sdiv ");
            case AND -> sb.append("and ");
            case OR -> sb.append("or ");
        }
        if (operands.size() == 2)
            sb.append(type.toString()).append(" ").append(operands.get(0).getName()).append(", ").append(operands.get(1).getName());
        else
            sb.append(type.toString()).append(" ").append("0 ").append(", ").append(operands.get(0).getName());
        return sb.toString();
    }

    @Override
    public void genMIPS() {
        RegDispatcher.getInstance().distributeRegFor(this);

    }
}
