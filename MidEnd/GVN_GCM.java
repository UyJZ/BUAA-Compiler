package MidEnd;

import IR_LLVM.LLVM_Module;
import IR_LLVM.LLVM_Values.BasicBlock;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Values.Function;
import IR_LLVM.LLVM_Values.Instr.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class GVN_GCM {

    private final LLVM_Module LLVMModule;

    HashMap<String, Instr> GVNMap;

    public GVN_GCM(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
        GVNMap = new HashMap<>();
    }

    public void run() {
        OptimizedCalc();
        GVN();
        GVNMap.clear();
    }

    private void GVN() {
        for (Function function : LLVMModule.getFunctionList()) {
            BasicBlock entry = function.getBlockArrayList().get(0);
            preOrderForGVN(entry);
        }
    }

    public void preOrderForGVN(BasicBlock block) {
        Iterator<Instr> iterator = block.getInstrs().iterator();
        LinkedHashSet<String> inserted = new LinkedHashSet<>();
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            if (instr instanceof IcmpInstr || instr instanceof BinaryInstr || instr instanceof GEPInstr || instr instanceof CallInstr callInstr && !callInstr.getFunction().hasSideEffect()) {
                if (GVNMap.containsKey(instr.GVNHash())) {
                    instr.replacedBy(GVNMap.get(instr.GVNHash()));
                    iterator.remove();
                } else {
                    GVNMap.put(instr.GVNHash(), instr);
                    inserted.add(instr.GVNHash());
                }
            }
        }
        for (BasicBlock block1 : block.getImmDominatee()) {
            if (block1 == block) continue;
            preOrderForGVN(block1);
        }
        for (String hash : inserted) {
            GVNMap.remove(hash);
        }
    }

    public void OptimizedCalc() {
        for (Function function : LLVMModule.getFunctionList()) {
            for (BasicBlock block : function.getBlockArrayList()) {
                LinkedHashSet<Instr> deadInstrSet = new LinkedHashSet<>();
                for (Instr instr : block.getInstrs()) {
                    if (instr instanceof BinaryInstr binaryInstr) {
                        switch (binaryInstr.getOpcode()) {
                            case ADD -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() + constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(1));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                            case SUB -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() - constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                            case MUL -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() * constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(1));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                            case SDIV -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() / constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                            case SREM -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() % constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                            case AND -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                    deadInstrSet.add(binaryInstr);
                                }
                            }
                        }
                    }
                }
                for (Instr instr : deadInstrSet) {
                    block.getInstrs().remove(instr);
                }
            }
        }
    }
}