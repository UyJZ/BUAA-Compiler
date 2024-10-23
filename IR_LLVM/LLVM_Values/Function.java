package IR_LLVM.LLVM_Values;

import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Value;
import MidEnd.GlobalForInline;
import MidEnd.RegDispatcher;
import IR_LLVM.LLVM_Builder;
import IR_LLVM.LLVM_Values.Instr.*;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.BranchInstr;
import IR_LLVM.LLVM_Values.Instr.terminatorInstr.ReturnInstr;
import IR_LLVM.LLVM_Types.*;

import java.util.*;

public class Function extends LLVM_Value {

    private ArrayList<Param> paramArrayList;

    private HashMap<AllocaInst, LinkedHashSet<Instr>> defMap;

    private HashMap<String, Instr> instrGVNMap;

    private boolean hasParam;

    private HashMap<LLVM_Value, Register> Val2Reg;

    private HashMap<Register, LLVM_Value> Reg2Val;

    private LinkedHashSet<Register> freeRegs;

    private LinkedHashSet<Register> freeArgRegs;

    private LinkedHashSet<Register> usedArgRegs;

    private LinkedHashSet<Register> usedRegs;

    private HashMap<LLVM_Value, Integer> Val2Offset;

    private boolean recursive;

    private boolean inlineAble = true;

    private final boolean isMainFunc;

    private final boolean isSysCall;

    private boolean hasPointer;

    private boolean hasSyscall;

    private boolean hasSideEffect;

    private int ValOffset;

    private LinkedHashSet<Function> callFunctions;

    public Function(LLVMType type, String name, boolean hasParam) {
        super(type, "@" + name);
        isMainFunc = name.equals("main");
        callFunctions = new LinkedHashSet<>();
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
        defMap = new HashMap<>();
        isSysCall = name.equals("getint") || name.equals("putint") || name.equals("putch") || name.equals("putstr");
        hasSideEffect = isSysCall;
        hasPointer = false;
        recursive = false;
        instrGVNMap = new HashMap<>();
    }

    private ArrayList<BasicBlock> blockArrayList;

    public void addBasicBlock(BasicBlock basicBlock) {
        if (blockArrayList.size() == 0) basicBlock.setFirstBlock();
        blockArrayList.add(basicBlock);
        basicBlock.setFather(this);
    }

    public boolean isMainFunc() {
        return isMainFunc;
    }

    public ArrayList<BasicBlock> getBlockArrayList() {
        return blockArrayList;
    }

    public void addParam(Param param) {
        paramArrayList.add(param);
        if (param.getType() instanceof PointerType || param.getType() instanceof ArrayType) {
            hasPointer = true;
        }
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

    public void setRecursive() {
        recursive = true;
    }

    public void setName() {
        LLVM_Builder.getInstance().setCurrentFunction(this);
        for (Param p : paramArrayList) {
            p.setName(LLVM_Builder.getInstance().genVirtualRegNum());
        }
        for (BasicBlock b : blockArrayList) {
            b.setName();
        }
    }

    public void clearName() {
        for (Param p : paramArrayList) {
            p.clearName();
        }
        for (BasicBlock b : blockArrayList) {
            b.clearName();
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

    public HashMap<LLVM_Value, Register> getVal2Reg() {
        return Val2Reg;
    }

    public HashMap<Register, LLVM_Value> getReg2Val() {
        return Reg2Val;
    }

    public LinkedHashSet<Register> getFreeRegs() {
        return freeRegs;
    }

    public LinkedHashSet<Register> getUsedRegs() {
        return usedRegs;
    }

    public HashMap<LLVM_Value, Integer> getVal2Offset() {
        return Val2Offset;
    }

    @Override
    public void genMIPS() {
        if (isSysCall) return;
        MipsBuilder.getInstance().addFunction(this);
        RegDispatcher.getInstance().enterFunc(this);
        distributeForArgs();
        for (BasicBlock b : blockArrayList) {
            b.genMIPS();
        }
        RegDispatcher.getInstance().leaveFunc();
    }

    public boolean isSysCall() {
        return isSysCall;
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

    public LinkedHashSet<Register> getFreeArgRegs() {
        return freeArgRegs;
    }

    public LinkedHashSet<Register> getUsedArgRegs() {
        return usedArgRegs;
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

    public ArrayList<AllocaInst> getAllAlloc() {
        ArrayList<AllocaInst> v = new ArrayList<>();
        for (BasicBlock block : blockArrayList) {
            v.addAll(block.getAllAlloc());
        }
        return v;
    }

    public void removeBlock(BasicBlock block) {
        blockArrayList.remove(block);
    }

    public void addCalledFunc(Function function) {
        callFunctions.add(function);
        callFunctions.addAll(function.getCallFunctions());
        //不可能出现循环调用所以无所谓
    }

    public LinkedHashSet<Function> getCallFunctions() {
        return callFunctions;
    }

    public void addDef(AllocaInst v, Instr instr) {
        if (!defMap.containsKey(v)) {
            defMap.put(v, new LinkedHashSet<>());
        }
        defMap.get(v).add(instr);
    }

    public boolean hasPointer() {
        return hasPointer;
    }

    public boolean HasSyscall() {
        for (BasicBlock b : blockArrayList) {
            for (Instr i : b.getInstrs()) {
                if (i instanceof CallInstr callInstr && callInstr.isIOInstr()) {
                    return true;
                } else if (i instanceof CallInstr callInstr && callInstr.getFunction().HasSyscall()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isInlineAble() {
        return inlineAble;
    }

    public void setInlineAble(boolean inlineAble) {
        this.inlineAble = inlineAble;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public InlinedFunc inline(ArrayList<LLVM_Value> params, BasicBlock outBlock, HashMap<LLVM_Value, BasicBlock> v2b) {
        ArrayList<GlobalVar> globalVars = GlobalForInline.globalVars;
        ArrayList<BasicBlock> blocks = new ArrayList<>();
        HashMap<LLVM_Value, LLVM_Value> map = new HashMap<>();
        for (GlobalVar globalVar : globalVars) {
            map.put(globalVar, globalVar);
        }
        for (BasicBlock block : blockArrayList) {
            map.put(block, new BasicBlock());
        }
        for (int i = 0; i < paramArrayList.size(); i++) {
            map.put(paramArrayList.get(i), params.get(i));
        }
        int len = blockArrayList.size();
        ArrayList<PhiInstr> phiToRebuild = new ArrayList<>();
        LinkedHashMap<BasicBlock, LLVM_Value> returnMap = new LinkedHashMap<>();
        for (int i = 0; i < len; i++) {
            BasicBlock currentBlock = blockArrayList.get(i);
            for (int j = 0; j < currentBlock.getInstrs().size(); j++) {
                Instr instr = currentBlock.getInstrs().get(j);
                if (instr instanceof CallInstr callInstr && !callInstr.getFunction().isSysCall && callInstr.getFunction().isInlineAble()) {
                    ArrayList<LLVM_Value> paramsForCall = new ArrayList<>();
                    for (LLVM_Value param : callInstr.getParam()) {
                        paramsForCall.add(param.copy(map));
                    }
                    BasicBlock nextBlock = new BasicBlock();
                    map.put(nextBlock, new BasicBlock());
                    InlinedFunc funcInline = callInstr.getFunction().inline(paramsForCall, (BasicBlock) nextBlock.copy(map), v2b);
                    ArrayList<BasicBlock> blocksToInsert = funcInline.getBlocks();
                    if (!(funcInline.getType() instanceof VoidType)) {
                        map.put(callInstr, funcInline.getPhi());
                    }
                    //TODO:先把call 变成 branch 然后再把block插入, 然后进行phi替换， 然后连上后面的部分
                    BranchInstr branch = new BranchInstr(blocksToInsert.get(0));
                    ((BasicBlock) currentBlock.copy(map)).addInstr(branch);
                    v2b.put(branch, (BasicBlock) currentBlock.copy(map));
                    blocks.add((BasicBlock) currentBlock.copy(map));
                    //对他进行替换,然后插入phi指令
                    //TODO:写的逻辑有点问题
                    if (!(blocksToInsert.get(blocksToInsert.size() - 1).lastInstr() instanceof BranchInstr)) {
                        blocksToInsert.get(blocksToInsert.size() - 1).addInstr(new BranchInstr((BasicBlock) nextBlock.copy(map)));
                        v2b.put(blocksToInsert.get(blocksToInsert.size() - 1).lastInstr(), blocksToInsert.get(blocksToInsert.size() - 1));
                    }
                    blocks.addAll(blocksToInsert);
                    for (BasicBlock block : blocksToInsert) {
                        map.put(block, block);
                        for (Instr instr1 : block.getInstrs()) {
                            map.put(instr1, instr1);
                        }
                    }
                    j++;
                    if (callInstr.getType() instanceof VoidType) {
                        for (; j < currentBlock.instrs.size(); j++) {
                            nextBlock.addInstr(currentBlock.instrs.get(j));
                        }
                        currentBlock = nextBlock;
                        j = -1;
                    } else {
                        ((BasicBlock) nextBlock.copy(map)).addInstr(funcInline.getPhi());
                        v2b.put(funcInline.getPhi(), (BasicBlock) nextBlock.copy(map));
                        map.put(funcInline.getPhi(), funcInline.getPhi());
                        for (; j < currentBlock.instrs.size(); j++) {
                            nextBlock.addInstr(currentBlock.instrs.get(j));
                        }
                        currentBlock = nextBlock;
                        j = -1;
                    }
                } else if (instr instanceof ReturnInstr returnInstr) {
                    if (!(type instanceof VoidType)) {
                        returnMap.put((BasicBlock) (currentBlock.copy(map)), returnInstr.getReturnValue().copy(map));
                    }
                    BranchInstr b = new BranchInstr(outBlock);
                    ((BasicBlock) (currentBlock.copy(map))).addInstr(b);
                    v2b.put(b, (BasicBlock) (currentBlock.copy(map)));
                    break;
                } else if (instr instanceof PhiInstr phiInstr) {
                    //TODO
                    phiToRebuild.add(phiInstr);
                    PhiInstr phiInstr1 = new PhiInstr(phiInstr.getType(), new AllocaInst(phiInstr.getType()));
                    map.put(phiInstr, phiInstr1);
                    ((BasicBlock) currentBlock.copy(map)).addInstr(phiInstr1);
                    v2b.put(phiInstr1, (BasicBlock) currentBlock.copy(map));
                } else {
                    Instr copy = instr.copy(map);
                    ((BasicBlock) (currentBlock.copy(map))).addInstr(copy);
                    v2b.put(copy, (BasicBlock) currentBlock.copy(map));
                    map.put(instr, copy);
                }
            }
            blocks.add((BasicBlock) currentBlock.copy(map));
        }
        for (PhiInstr phiInstr : phiToRebuild) {
            for (int i = 0; i < phiInstr.getValues().size(); i++) {
                BasicBlock block = v2b.get(phiInstr.getLabels().get(i).lastInstr().copy(map));
                if (phiInstr.getLabels().get(i).lastInstr().copy(map) instanceof BranchInstr branchInstr) {
                    if (!branchInstr.getSuccessors().contains(v2b.get(phiInstr.copy(map)))) {
                        System.out.println("error");
                    }
                }
                ((PhiInstr) phiInstr.copy(map)).addOption(block, phiInstr.getValues().get(i).copy(map));
            }
        }
        PhiInstr phi = new PhiInstr(type, new AllocaInst(type));
        for (BasicBlock block : returnMap.keySet()) {
            phi.addOption(block, returnMap.get(block));
        }
        if (!(blocks.get(blocks.size() - 1).getInstrs().get(blocks.get(blocks.size() - 1).getInstrs().size() - 1) instanceof BranchInstr)) {
            BranchInstr branchInstr = new BranchInstr(outBlock);
            blocks.get(blocks.size() - 1).addInstr(branchInstr);
            v2b.put(branchInstr, blocks.get(blocks.size() - 1));
        }
        return new InlinedFunc(type, blocks, phi);
    }

    public void addBasicBlock(int pos, BasicBlock block) {
        blockArrayList.add(pos, block);
        block.setFather(this);
    }

    public HashMap<String, Instr> getInstrGVNMap() {
        return instrGVNMap;
    }

    @Override
    public boolean isDistributable() {
        return false;
    }

    public boolean hasSideEffect() {
        return hasSideEffect;
    }

    public void buildSideEffect() {
        if (isSysCall) {
            hasSideEffect = true;
            return;
        }
        for (Param param : paramArrayList) {
            if (param.getType() instanceof ArrayType || param.getType() instanceof PointerType) {
                hasSideEffect = true;
                return;
            }
        }
        for (BasicBlock block : blockArrayList) {
            for (Instr instr : block.getInstrs()) {
                if (instr instanceof CallInstr callInstr && callInstr.getFunction() != this && callInstr.getFunction().hasSideEffect()) {
                    hasSideEffect = true;
                    return;
                }
                if (instr instanceof StoreInstr storeInstr && storeInstr.getDst() instanceof GlobalVar) {
                    hasSideEffect = true;
                    return;
                }
                if (instr instanceof LoadInstr loadInstr && loadInstr.getPtr() instanceof GlobalVar) {
                    hasSideEffect = true;
                    return;
                }
            }
        }
        hasSideEffect = false;
    }
}