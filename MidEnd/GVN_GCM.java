package MidEnd;

import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Function;
import llvm_ir.Values.Instruction.BinaryInstr;
import llvm_ir.Values.Instruction.GEPInstr;
import llvm_ir.Values.Instruction.IcmpInstr;
import llvm_ir.Values.Instruction.Instr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class GVN_GCM {

    private final Module module;

    HashMap<String, Instr> GVNMap;

    public GVN_GCM(Module module) {
        this.module = module;
        GVNMap = new HashMap<>();
    }

    public void run() {
        GVN();
    }

    private void GVN() {
        for (Function function : module.getFunctionList()) {
            BasicBlock entry = function.getBlockArrayList().get(0);
            preOrderForGVN(entry);
        }
    }

    public void preOrderForGVN(BasicBlock block) {
        Iterator<Instr> iterator = block.getInstrs().iterator();
        LinkedHashSet<String> inserted = new LinkedHashSet<>();
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            if (instr instanceof IcmpInstr || instr instanceof BinaryInstr || instr instanceof GEPInstr) {
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
        for (Function function : module.getFunctionList()) {
            for (BasicBlock block : function.getBlockArrayList()) {
                for (Instr instr : block.getInstrs()) {
                    if (instr instanceof BinaryInstr binaryInstr) {
                        switch (binaryInstr.getOpcode()) {
                            case ADD -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() + constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(1));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                }
                            }
                            case SUB -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() - constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                }
                            }
                            case MUL -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() * constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 0) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                } else if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(1));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                }
                            }
                            case SDIV -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() / constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(binaryInstr.getOperands().get(0));
                                }
                            }
                            case SREM -> {
                                if (binaryInstr.getOperands().get(0) instanceof ConstInteger constInteger1 && binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger2) {
                                    int res = constInteger1.getVal() % constInteger2.getVal();
                                    binaryInstr.replacedBy(new ConstInteger(res));
                                } else if (binaryInstr.getOperands().get(1) instanceof ConstInteger constInteger1 && constInteger1.getVal() == 1) {
                                    binaryInstr.replacedBy(new ConstInteger(0));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}