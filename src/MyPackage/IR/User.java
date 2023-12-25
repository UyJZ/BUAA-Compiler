package MyPackage.IR;

import java.util.ArrayList;

public class User extends Value{
    private ArrayList<Value> operands;

    public User(Type type, int value) {
        super(type, value);
        operands = new ArrayList<>();
    }

    public void addOperand(Value value) {
        operands.add(value);
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }

    public int getPos() {
        return operands.size() - 1;
    }

    public void setOperands(ArrayList<Value> operands) {
        this.operands = operands;
    }
}
