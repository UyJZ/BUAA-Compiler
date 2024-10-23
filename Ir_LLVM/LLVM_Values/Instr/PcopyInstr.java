package Ir_LLVM.LLVM_Values.Instr;


import Config.Tasks;
import Ir_LLVM.LLVM_Value;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Types.Integer32Type;

import java.util.ArrayList;
import java.util.HashMap;


public class PcopyInstr extends Instr{

    private ArrayList<LLVM_Value> dstList;

    private ArrayList<LLVM_Value> srcList;

    private HashMap<LLVM_Value, LLVM_Value> map;

    public PcopyInstr() {
        super(new Integer32Type(), Tasks.isSetNameAfterGen ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        dstList = new ArrayList<>();
        srcList = new ArrayList<>();
        map = new HashMap<>();
    }

    public void addCopy(LLVM_Value dst, LLVM_Value src) {
        dstList.add(dst);
        srcList.add(src);
        map.put(dst, src);
    }
    //反正这玩意也要消掉就不留def-use链了

    public LLVM_Value getSrcOf(LLVM_Value dst) {
        return map.get(dst);
    }

    public LLVM_Value getDstOf(LLVM_Value src) {
        for (int i = 0; i < srcList.size(); i++) {
            if (srcList.get(i) == src) return dstList.get(i);
        }
        return null;
    }

    public void removeCopy(LLVM_Value dst) {
        int index = dstList.indexOf(dst);
        dstList.remove(index);
        srcList.remove(index);
        map.remove(dst);
    }

    public void replaceCopy(LLVM_Value dst, LLVM_Value src, LLVM_Value src_new) {
        int index = dstList.indexOf(dst);
        srcList.set(index, src_new);
        map.put(dst, src_new);
    }

    public int size() {
        return dstList.size();
    }

    public ArrayList<LLVM_Value> getDstList() {
        return dstList;
    }

    public ArrayList<LLVM_Value> getSrcList() {
        return srcList;
    }

    @Override
    public void setName() {
        for (int i = 0; i < dstList.size(); i++) {
            if (dstList.get(i).getName().length() == 0) {
                dstList.get(i).setName(LLVM_Builder.getInstance().genVirtualRegNum());
            }
            if (srcList.get(i).getName().length() == 0) {
                srcList.get(i).setName(LLVM_Builder.getInstance().genVirtualRegNum());
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
