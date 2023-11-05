package llvm_ir;

import llvm_ir.llvmType.LLVMType;

import java.util.ArrayList;

public class Value {

    protected ArrayList<User> userList;

    protected String name;

    protected LLVMType type;

    public String getId() {
        return null;
    }

    public Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.userList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LLVMType getType() {
        return type;
    }

}
