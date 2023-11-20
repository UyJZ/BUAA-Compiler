package llvm_ir.Values;

import BackEnd.MIPS.Assembly.BlockAsm;
import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.LabelAsm;
import BackEnd.MIPS.MipsController;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.Instruction.CallInstr;
import llvm_ir.Values.Instruction.Instr;
import llvm_ir.llvmType.BasicBlockType;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BasicBlock extends Value {
    private LabelAsm label;

    private Function father;

    private Instr terminator;

    private BlockAsm blockAsm;

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
        this.label = new LabelAsm(father.getName() + "_" + father.getOrderOf(this));
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

    public LabelAsm getMIPSLabel() {
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


    public void addPosBlock(BasicBlock block) {
        this.posBlocks.add(block);
    }

    @Override
    public void genMIPS() {
        MipsController.getInstance().addBasicBlock(this);
        for (int i = 0; i < instrs.size(); i++) {
            if (instrs.get(i) instanceof CallInstr callInstr && callInstr.isOutputStrInstr()) {
                //TODO:output string
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
                    sb.append(c);
                    i++;
                }
                Data data1 = new Data(sb.toString());
                callInstr.setConStrName(data1.getName());
            }
        }
    }
}
