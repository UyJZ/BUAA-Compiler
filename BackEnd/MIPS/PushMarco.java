package BackEnd.MIPS;

import llvm_ir.Value;

import java.util.ArrayList;

public class PushMarco {
    private final ArrayList<Value> operands;

    public PushMarco(ArrayList<Value> operands) {
        this.operands = new ArrayList<>();
        for (int i = operands.size() - 1; i >= 0; i--) {
            this.operands.add(operands.get(i));
        }
    }
}
