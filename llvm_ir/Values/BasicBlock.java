package llvm_ir.Values;

import BackEnd.MIPS.Assembly.BlockAsm;
import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsController;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.*;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.Integer32Type;

import java.util.*;

public class BasicBlock extends Value {
    private LabelAsm label;

    private Function father;

    private Instr terminator;

    private BlockAsm blockAsm;

    private LinkedHashSet<BasicBlock> dominatee;

    private LinkedHashSet<BasicBlock> dominators;

    private LinkedHashSet<BasicBlock> strictDominatee;

    private LinkedHashSet<BasicBlock> strictDominator;

    private LinkedHashSet<BasicBlock> dominateFrontier;

    private BasicBlock ImmDominator;

    private HashSet<Value> inSet;

    private HashSet<Value> outSet;

    private HashSet<Value> defSet;

    private final ArrayList<BasicBlock> posBlocks = new ArrayList<>();

    private final ArrayList<BasicBlock> preBlocks = new ArrayList<>();
    private boolean isFirstBlock;

    public BasicBlock() {
        super(new BasicBlockType(), "");
        this.father = IRController.getInstance().getCurrentFunction();
        instrs = new ArrayList<>();
        this.isFirstBlock = false;
        dominatee = new LinkedHashSet<>();
        dominatee.add(this);
        dominators = new LinkedHashSet<>();
        strictDominatee = new LinkedHashSet<>();
        dominateFrontier = new LinkedHashSet<>();
    }


    public void addPreBlock(BasicBlock preBlock) {
        preBlocks.add(preBlock);
    }

    private int varCnt = 0;
    protected ArrayList<Instr> instrs;

    public void setFirstBlock() {
        this.isFirstBlock = true;
    }

    public LabelAsm getMIPSLabel() {
        if (label == null) label = new LabelAsm(father.getName().substring(1) + "_" + father.getOrderOf(this));
        return label;
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

    public void setName() {
        this.name = IRController.getInstance().genVirtualRegNum();
        for (Instr instr : instrs) {
            instr.setName();
        }
    }

    public void setInSet(HashSet<Value> set) {
        inSet = set;
    }

    public void setOutSet(HashSet<Value> set) {
        outSet = set;
    }

    public void setDefSet(HashSet<Value> set) {
        defSet = set;
    }

    public HashSet<Value> getInSet() {
        return inSet;
    }

    public HashSet<Value> getDefSet() {
        return defSet;
    }

    public HashSet<Value> getOutSet() {
        return outSet;
    }

    public void BuildDefUse() {
        defSet = new HashSet<>();
        outSet = new HashSet<>();
        for (Instr instr : instrs) {
            for (Value operand : instr.getOperands()) {
                if (operand instanceof Instr || operand instanceof Param || operand instanceof GlobalVar) {
                    outSet.add(operand);
                }
            }
            if (instr.hasOutput()) {
                defSet.add(instr);
            }
        }
    }

    public void addPreDominator(BasicBlock block) {
        if (!block.equals(this)) dominators.add(block);
    }


    public void addPosBlock(BasicBlock block) {
        this.posBlocks.add(block);
    }

    @Override
    public void genMIPS() {
        MipsController.getInstance().addBasicBlock(this);
        for (int i = 0; i < instrs.size(); i++) {
            if (instrs.get(i) instanceof CallInstr callInstr && callInstr.isOutputStrInstr()) {
                //TODO:output string
                callInstr.genMIPS();
                while (i < instrs.size() && instrs.get(i) instanceof CallInstr callInstr1 && callInstr1.isOutputStrInstr()) {
                    i++;
                }
                i--;
            } else {
                instrs.get(i).genMIPS();
            }
        }
    }

    public void setBlockAsm(BlockAsm blockAsm) {
        this.blockAsm = blockAsm;
    }

    @Override
    public void genConStr() {
        for (int i = 0; i < instrs.size(); i++) {
            if (instrs.get(i) instanceof CallInstr callInstr && callInstr.isOutputStrInstr()) {
                StringBuilder sb = new StringBuilder();
                callInstr.setConStrHead();
                while (i < instrs.size() && instrs.get(i) instanceof CallInstr callInstr1 && callInstr1.isOutputStrInstr()) {
                    char c = (char) callInstr1.getValForOutput();
                    if (c == '\n') sb.append('\\').append('n');
                    else sb.append(c);
                    i++;
                }
                Data data1 = new Data(sb.toString());
                MipsController.getInstance().addGlobalVar(data1);
                callInstr.setConStrName(data1.getName());
            }
        }
    }

    public ArrayList<BasicBlock> getPreBlocks() {
        return preBlocks;
    }

    public ArrayList<BasicBlock> getPosBlocks() {
        return posBlocks;
    }

    public LinkedHashSet<BasicBlock> getDominatee() {
        return dominatee;
    }

    public void setDominatee(LinkedHashSet<BasicBlock> dominatee) {
        this.dominatee = dominatee;
        this.dominatee.add(this);
        for (BasicBlock block : dominatee) {
            block.addPreDominator(this);
        }
    }

    public boolean isStrictDominator(BasicBlock block) {
        return strictDominator.contains(block);
    }

    public void DFSBuildStrictDominator(BasicBlock target, ArrayList<BasicBlock> road, ArrayList<ArrayList<BasicBlock>> roads) {
        if (this.equals(target)) {
            road.add(this);
            roads.add(new ArrayList<>(road));
        } else if (road.contains(this)) {
            road.add(this);
            for (BasicBlock b : dominatee) {
                if (!road.contains(b)) b.DFSBuildStrictDominator(target, road, roads);
            }
            road.remove(this);
        } else {
            road.add(this);
            for (BasicBlock block : dominatee) {
                block.DFSBuildStrictDominator(target, road, roads);
            }
            road.remove(this);
        }
    }

    public void setStrictDominator(LinkedHashSet<BasicBlock> strictDominator) {
        this.strictDominator = strictDominator;
        for (BasicBlock block : strictDominator) {
            block.addStrictDominatee(this);
        }
    }

    public void addStrictDominatee(BasicBlock block) {
        strictDominatee.add(block);
    }

    public void buildImmDominator() {
        if (isFirstBlock) return;
        ArrayList<BasicBlock> blocks = new ArrayList<>(this.strictDominator);
        for (int i = 0; i < blocks.size(); i++) {
            boolean isImm = true;
            for (int j = 0; j < blocks.size(); j++) {
                if (i != j && blocks.get(i).isStrictDominator(blocks.get(j))) {
                    isImm = false;
                    break;
                }
            }
            if (isImm) {
                ImmDominator = blocks.get(i);
                break;
            }
        }
    }

    public LinkedHashSet<BasicBlock> getStrictDominatee() {
        return strictDominatee;
    }

    public LinkedHashSet<BasicBlock> getStrictDominator() {
        return strictDominator;
    }

    public BasicBlock getImmDominator() {
        return ImmDominator;
    }

    public LinkedHashSet<BasicBlock> getDominateFrontier() {
        return dominateFrontier;
    }

    public void setDominateFrontier(LinkedHashSet<BasicBlock> set) {
        dominateFrontier = set;
    }

    public ArrayList<AllocaInst> getValForSSA() {
        ArrayList<AllocaInst> v = new ArrayList<>();
        for (Instr instr : instrs) {
            if (instr instanceof AllocaInst allocaInst && allocaInst.getElementType() instanceof Integer32Type)
                v.add(allocaInst);
        }
        return v;
    }

    public boolean isDefBlockFor(AllocaInst v) {
        for (Instr instr : instrs) {
            if (instr instanceof StoreInstr storeInstr && storeInstr.getDst().equals(v)) return true;
        }
        return false;
    }

    public boolean isUseBlockFor(AllocaInst v) {
        for (Instr i : instrs) {
            if (i instanceof LoadInstr load && load.getPtr().equals(v)) return true;
        }
        return false;
    }

    public void insertPhi(PhiInstr phi) {
        this.instrs.add(0, phi);
    }

    public void preOrderForRename(AllocaInst v, Stack<Value> stack) {
        int cnt = 0;
        for (Instr instr : instrs) {
            if (instr instanceof AllocaInst allocaInst && allocaInst == v) {
                allocaInst.delete();
                instrs.remove(allocaInst);
            } else if (instr instanceof StoreInstr storeInstr && storeInstr.getDst() == v) {
                stack.push(storeInstr.getSrc());
                cnt++;
                storeInstr.delete();
                instrs.remove(storeInstr);
            } else if (instr instanceof LoadInstr loadInstr && loadInstr.getPtr() == v) {
                //TODO:replace all load to the stack peek
                for (Instr instr1 : instrs) {
                    instr1.replaceValue(loadInstr, stack.peek());
                }
                loadInstr.delete();
                instrs.remove(loadInstr);
            } else if (instr instanceof PhiInstr phiInstr && phiInstr.getFather() == v) {
                stack.push(phiInstr);
                cnt++;
            }
        }
        for (BasicBlock block : dominatee) {
            block.preOrderForRename(v, stack);
        }
        for (BasicBlock block : posBlocks) {
            block.addPhiOption(v, stack.peek());
        }
        for (int i = 0; i < cnt; i++) {
            stack.pop();
        }
    }

    public void addPhiOption(AllocaInst v, Value value) {
        for (Instr instr : instrs) {
            if (instr instanceof PhiInstr phiInstr && phiInstr.getFather() == v) {
                phiInstr.addOption(this, value);
            }
        }
    }
}
