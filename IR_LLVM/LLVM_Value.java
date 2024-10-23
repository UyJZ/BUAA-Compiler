package IR_LLVM;

import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Register;
import IR_LLVM.LLVM_Values.ConstInteger;
import IR_LLVM.LLVM_Types.LLVMType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class LLVM_Value {

    protected ArrayList<LLVM_User> LLVMUserList;

    protected ArrayList<LLVM_User> usedByList;

    protected String name;

    public static int cnt = 0;

    protected LLVMType type;

    protected boolean useReg;

    protected boolean isDistributed;

    protected Register register;

    protected Data data;

    protected boolean isExist;

    protected boolean isMem;

    public String hash;

    protected int offset;

    public String getId() {
        return null;
    }

    public LLVM_Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.LLVMUserList = new ArrayList<>();
        isDistributed = false;
        isExist = true;
        isMem = false;
        usedByList = new ArrayList<>();
        if (this instanceof ConstInteger constInteger) {
            hash = "_" + name;
        } else
            hash = "_H" + cnt++;
    }

    public String getName() {
        return name;
    }

    public LLVMType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void clearName() {
        if (!(this instanceof ConstInteger)) this.name = "";
    }

    public int getLen() {
        return type.getLen();
    }

    public void setUseReg(Register register) {
        this.register = register;
        this.useReg = true;
        this.isDistributed = true;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        this.useReg = false;
        this.isDistributed = true;
        this.isMem = true;
    }

    public Register getRegister() {
        return register;
    }

    public int getOffset() {
        return offset;
    }

    public void genMIPS() {
        return;
    }

    public boolean isUseReg() {
        return useReg;
    }

    public boolean isDistributed() {
        return isDistributed;
    }

    public void genConStr() {
        return;
    }

    public void delete() {
        isExist = false;
    }

    public void addUsedBy(LLVM_User value) {
        this.usedByList.add(value);
    }

    public void replacedBy(LLVM_Value LLVMValue) {
        ArrayList<LLVM_User> toAdd = new ArrayList<>();
        for (LLVM_User LLVMUser : usedByList) {
            boolean b = LLVMUser.replaceValue(this, LLVMValue);
            if (b) {
                toAdd.add(LLVMUser);
            }
        }
        for (LLVM_User value1 : toAdd) {
            LLVMValue.addUsedBy(value1);
        }
    }

    public void DFSForUseful(LinkedHashSet<LLVM_Value> usefulInstr) {
        if (usefulInstr.contains(this)) return;
        usefulInstr.add(this);
        if (this instanceof LLVM_User LLVMUser) {
            for (LLVM_Value v : LLVMUser.getOperands()) {
                v.DFSForUseful(usefulInstr);
            }
        }
        for (LLVM_Value v : this.usedByList) {
            v.DFSForUseful(usefulInstr);
        }
    }

    public LLVM_Value copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return map.get(this);
        return null;
    }

    public void removeDistribute() {
        this.isDistributed = true;
        this.useReg = false;
        this.isMem = true;
        offset = 4;
    }

    public boolean isDistributable() {
        return true;
    }

    public boolean isMem() {
        return isMem;
    }

    public boolean isDistributedToReg() {
        return isDistributed && useReg;
    }

    public boolean isDistributedToMem() {
        return isDistributed && isMem;
    }

}
