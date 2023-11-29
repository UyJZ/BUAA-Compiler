package llvm_ir;

import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Register;
import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class Value {

    protected ArrayList<User> userList;

    protected ArrayList<User> usedByList;

    protected String name;

    public static int cnt = 0;

    protected LLVMType type;

    protected boolean useReg;

    protected boolean isDistributed;

    protected Register register;

    protected Data data;

    protected boolean isExist;

    public int hash;

    protected int offset;

    public String getId() {
        return null;
    }

    public Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.userList = new ArrayList<>();
        isDistributed = false;
        isExist = true;
        usedByList = new ArrayList<>();
        hash = cnt++;
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

    public boolean isExist() {
        return isExist;
    }

    public void addUsedBy(User value) {
        this.usedByList.add(value);
    }

    public void replacedBy(Value value) {
        ArrayList<User> toAdd = new ArrayList<>();
        for (User user : usedByList) {
            boolean b = user.replaceValue(this, value);
            if (b) {
                toAdd.add(user);
            }
        }
        for (User value1 : toAdd) {
            value.addUsedBy(value1);
        }
    }

}
