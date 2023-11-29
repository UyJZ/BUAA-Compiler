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

    private boolean isReachAble;

    private Function father;

    private Instr terminator;

    private BlockAsm blockAsm;

    private LinkedHashSet<BasicBlock> dominatee;

    private LinkedHashSet<BasicBlock> dominators;

    private LinkedHashSet<BasicBlock> strictDominatee;

    private LinkedHashSet<BasicBlock> strictDominator;

    private LinkedHashSet<BasicBlock> dominateFrontier;

    private BasicBlock ImmDominator;

    private LinkedHashSet<BasicBlock> ImmDominatee;

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
        strictDominator = new LinkedHashSet<>();
        dominateFrontier = new LinkedHashSet<>();
        isReachAble = true;
        ImmDominatee = new LinkedHashSet<>();
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

    public void addDominatee(BasicBlock block) {
        dominatee.add(block);
    }

    public void setDominators(LinkedHashSet<BasicBlock> dominators) {
        this.dominators = new LinkedHashSet<>(dominators);
        this.dominators.add(this);
        for (BasicBlock block : dominators) {
            block.addDominatee(this);
        }
    }

    public boolean isStrictDominatorOf(BasicBlock block) {
        return strictDominatee.contains(block);
    }

    public void DFSBuildStrictDominator(BasicBlock target, ArrayList<BasicBlock> road, ArrayList<ArrayList<BasicBlock>> roads) {
        if (this.equals(target)) {
            road.add(this);
            roads.add(new ArrayList<>(road));
            road.remove(this);
        } else if (road.contains(this)) {
            return;
        } else {
            road.add(this);
            for (BasicBlock block : posBlocks) {
                block.DFSBuildStrictDominator(target, road, roads);
            }
            road.remove(this);
        }
    }

    public void setStrictDominator(LinkedHashSet<BasicBlock> strictDominator) {
        this.strictDominator = new LinkedHashSet<>(strictDominator);
        this.strictDominator.remove(this);
        for (BasicBlock block : this.strictDominator) {
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
                if (i != j && blocks.get(i).isDominatorOf(blocks.get(j))) {
                    isImm = false;
                    break;
                }
            }
            if (isImm) {
                ImmDominator = blocks.get(i);
                blocks.get(i).addImmDominatee(this);
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
        dominateFrontier = new LinkedHashSet<>(set);
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
        for (Instr instr : instrs) {
            if (instr instanceof PhiInstr phiInstr && phiInstr.getFather() == phi.getFather()) return;
        }
        this.instrs.add(0, phi);
    }

    public void preOrderForRename(AllocaInst v, Stack<Value> stack) {
        int cnt = 0;
        Iterator<Instr> iterator = instrs.iterator();
        if (v.getAllocaNum() == 2) {
            System.out.println("DEBUG");
        }
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            if (instr instanceof AllocaInst allocaInst && allocaInst == v) {
                allocaInst.delete();
                iterator.remove();
            } else if (instr instanceof StoreInstr storeInstr && storeInstr.getDst() == v) {
                stack.push(storeInstr.getSrc());
                cnt++;
                storeInstr.delete();
                iterator.remove();
            } else if (instr instanceof LoadInstr loadInstr && loadInstr.getPtr() == v) {
                //TODO:replace all load to the stack peek
                if (loadInstr.hash == 20) {
                    System.out.println("HERE");
                }
                loadInstr.delete();
                iterator.remove();
                /*
                for (BasicBlock block : father.getBlockArrayList()) {
                    for (Instr instr1 : block.getInstrs()) {
                        instr1.replacedBy(stack.isEmpty() ? new UndefinedVal() : stack.peek());
                    }
                }
                 */
                loadInstr.replacedBy(stack.isEmpty() ? new UndefinedVal() : stack.peek());
            } else if (instr instanceof PhiInstr phiInstr && phiInstr.getFather() == v) {
                stack.push(phiInstr);
                cnt++;
            }
        }
        for (BasicBlock block : posBlocks) {
            if (stack.isEmpty()) {
                System.out.println("HERE");
            }
            block.addPhiOption(v, stack.isEmpty() ? new UndefinedVal() : stack.peek(), this);
        }
        for (BasicBlock block : ImmDominatee) {
            if (block == this) continue;
            block.preOrderForRename(v, stack);
        }
        for (int i = 0; i < cnt; i++) {
            stack.pop();
        }
    }

    public void addPhiOption(AllocaInst v, Value value, BasicBlock block) {
        for (Instr instr : instrs) {
            if (instr instanceof PhiInstr phiInstr && phiInstr.getFather() == v) {
                phiInstr.addOption(block, value);
            }
        }
    }

    public void deleteBlock() {
        this.isReachAble = false;
        for (BasicBlock block : posBlocks) {
            block.removePreBlock(this);
        }
        for (BasicBlock block : preBlocks) {
            block.removePosBlock(this);
        }
    }

    public void removePreBlock(BasicBlock block) {
        preBlocks.remove(block);
    }

    public void removePosBlock(BasicBlock block) {
        posBlocks.remove(block);
    }

    public void deleteInfo(BasicBlock block) {

    }

    public void setUnReachAble() {
        this.isReachAble = false;
    }

    public boolean isReachAble() {
        return isReachAble;
    }

    public boolean isDominatorOf(BasicBlock block) {
        return dominatee.contains(block);
    }

    public ArrayList<Instr> getInstrs() {
        return instrs;
    }

    public void addImmDominatee(BasicBlock block) {
        ImmDominatee.add(block);
    }
}
