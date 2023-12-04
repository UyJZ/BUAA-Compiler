package llvm_ir.Values.Instruction;


import Config.tasks;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.llvmType.Integer32Type;

import java.util.ArrayList;
import java.util.HashMap;


public class PcopyInstr extends Instr{

    private ArrayList<Value> dstList;

    private ArrayList<Value> srcList;

    private HashMap<Value, Value> map;

    public PcopyInstr() {
        super(new Integer32Type(), tasks.isSetNameAfterGen ? "" : IRController.getInstance().genVirtualRegNum());
        dstList = new ArrayList<>();
        srcList = new ArrayList<>();
        map = new HashMap<>();
    }

    public void addCopy(Value dst, Value src) {
        dstList.add(dst);
        srcList.add(src);
        map.put(dst, src);
    }
    //反正这玩意也要消掉就不留def-use链了

    public Value getSrcOf(Value dst) {
        return map.get(dst);
    }

    public Value getDstOf(Value src) {
        for (int i = 0; i < srcList.size(); i++) {
            if (srcList.get(i) == src) return dstList.get(i);
        }
        return null;
    }

    public void removeCopy(Value dst) {
        int index = dstList.indexOf(dst);
        dstList.remove(index);
        srcList.remove(index);
        map.remove(dst);
    }

    public void replaceCopy(Value dst, Value src, Value src_new) {
        int index = dstList.indexOf(dst);
        srcList.set(index, src_new);
        map.put(dst, src_new);
    }

    public int size() {
        return dstList.size();
    }

    public ArrayList<Value> getDstList() {
        return dstList;
    }

    public ArrayList<Value> getSrcList() {
        return srcList;
    }

    @Override
    public void setName() {
        for (int i = 0; i < dstList.size(); i++) {
            if (dstList.get(i).getName().length() == 0) {
                dstList.get(i).setName(IRController.getInstance().genVirtualRegNum());
            }
            if (srcList.get(i).getName().length() == 0) {
                srcList.get(i).setName(IRController.getInstance().genVirtualRegNum());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("pcopy ");
        for (int i = 0; i < dstList.size(); i++) {
            sb.append(dstList.get(i).getName()).append(" <- ").append(srcList.get(i).getName());
            if (i != dstList.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
