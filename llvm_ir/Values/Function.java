package llvm_ir.Values;

import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.AllocaInst;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.Values.Instruction.terminatorInstr.ReturnInstr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Function extends Value {

    private ArrayList<Param> paramArrayList;

    private boolean hasParam;

    private HashMap<Value, Register> Val2Reg;

    private HashMap<Register, Value> Reg2Val;

    private LinkedHashSet<Register> freeRegs;

    private LinkedHashSet<Register> freeArgRegs;

    private LinkedHashSet<Register> usedArgRegs;

    private LinkedHashSet<Register> usedRegs;

    private HashMap<Value, Integer> Val2Offset;

    private final boolean isMainFunc;

    private final boolean isSysCall;

    private int ValOffset;

    public Function(LLVMType type, String name, boolean hasParam) {
        super(type, "@" + name);
        isMainFunc = name.equals("main");
        paramArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
        this.hasParam = hasParam;
        ValOffset = 0;
        offset = 0;
        Val2Reg = new HashMap<>();
        Reg2Val = new HashMap<>();
        freeArgRegs = Register.argsRegs();
        usedArgRegs = new LinkedHashSet<>();
        usedRegs = new LinkedHashSet<>();
        freeRegs = Register.tempRegs();
        Val2Offset = new HashMap<>();
        isSysCall = name.equals("getint") || name.equals("putint") || name.equals("putch") || name.equals("putstr");
    }

    private ArrayList<BasicBlock> blockArrayList;

    public void addBasicBlock(BasicBlock basicBlock) {
        if (blockArrayList.size() == 0) basicBlock.setFirstBlock();
        blockArrayList.add(basicBlock);
    }

    public boolean isMainFunc() {
        return isMainFunc;
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

    public void setName() {
        IRController.getInstance().setCurrentFunction(this);
        for (Param p : paramArrayList) {
            p.setName(IRController.getInstance().genVirtualRegNum());
        }
        for (BasicBlock b : blockArrayList) {
            b.setName();
        }
    }

    public void allocSpace(int size) {
        ValOffset += size;
    }

    public int getOrderOf(BasicBlock block) {
        return blockArrayList.indexOf(block);
    }

    public int getValOffset() {
        return ValOffset;
    }

    public HashMap<Value, Register> getVal2Reg() {
        return Val2Reg;
    }

    public HashMap<Register, Value> getReg2Val() {
        return Reg2Val;
    }

    public LinkedHashSet<Register> getFreeRegs() {
        return freeRegs;
    }

    public LinkedHashSet<Register> getUsedRegs() {
        return usedRegs;
    }

    public HashMap<Value, Integer> getVal2Offset() {
        return Val2Offset;
    }

    @Override
    public void genMIPS() {
        if (isSysCall) return;
        MipsController.getInstance().addFunction(this);
        RegDispatcher.getInstance().enterFunc(this);
        distributeForArgs();
        for (BasicBlock b : blockArrayList) {
            b.genMIPS();
        }
        RegDispatcher.getInstance().leaveFunc();
    }

    private void distributeForArgs() {
        for (Param param : paramArrayList) {
            if (!param.isDistributed()) {
                if (freeArgRegs.isEmpty()) {
                    offset -= 4;
                    param.setOffset(offset);
                    RegDispatcher.getInstance().allocSpaceForReg();
                } else {
                    Register reg = freeArgRegs.iterator().next();
                    freeArgRegs.remove(reg);
                    usedArgRegs.add(reg);
                    param.setUseReg(reg);
                }
            }
        }
    }

    @Override
    public void genConStr() {
        for (BasicBlock b : blockArrayList) {
            b.genConStr();
        }
    }

    public void deleteBlock(BasicBlock block) {
        blockArrayList.remove(block);
    }

    public ArrayList<AllocaInst> getValForSSA() {
        ArrayList<AllocaInst> v = new ArrayList<>();
        for (BasicBlock block : blockArrayList) {
            v.addAll(block.getValForSSA());
        }
        return v;
    }

    public void removeBlock(BasicBlock block) {
        blockArrayList.remove(block);
    }
}