package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.PopMarco;
import BackEnd.MIPS.PushMarco;
import BackEnd.MIPS.Register;
import Config.tasks;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.Values.Function;
import llvm_ir.Values.Param;
import llvm_ir.llvmType.Integer32Type;
import llvm_ir.llvmType.LLVMType;
import llvm_ir.llvmType.VoidType;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class CallInstr extends Instr {

    private Function function;

    private final String functionName;

    private final boolean isIOInstr;

    private final boolean isInputInstr;

    private final boolean isOutputInstr;

    private final boolean isOutputStrInstr;

    private boolean isConStrHead;

    private String ConstrName;

    private int val;

    public CallInstr(LLVMType type, Value func, ArrayList<Value> params) {
        //库函数
        super(type, type instanceof VoidType || tasks.isIsOptimize() ? "" : IRController.getInstance().genVirtualRegNum());
        functionName = func.getName();
        isIOInstr = functionName.equals("@getint") || functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        isInputInstr = functionName.equals("@getint");
        isOutputInstr = functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        isOutputStrInstr = functionName.equals("@putstr") || functionName.equals("@putch");
        for (Value v : params) this.addValue(v);
        if (functionName.equals("@putch")) {
            val = ((ConstInteger) params.get(0)).getVal();
        }
    }

    @Override
    public String toString() {
        String FuncName = (function == null) ? functionName : function.getName();
        StringBuilder sb = new StringBuilder();
        if (type instanceof VoidType) {
            sb.append("call ").append(type.toString()).append(" ").append(FuncName).append("(");
        } else {
            sb.append(name).append(" = call ").append(type.toString()).append(" ").append(FuncName).append("(");
        }
        for (int i = 0; i < operands.size(); i++) {
            sb.append(operands.get(i).getType().toString()).append(" ").append(operands.get(i).getName());
            if (i != operands.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isInputInstr() {
        return isInputInstr;
    }

    public boolean isOutputInstr() {
        return isOutputInstr;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setConStrHead() {
        isConStrHead = true;
    }

    public boolean isIOInstr() {
        return isIOInstr;
    }

    public boolean isOutputStrInstr() {
        return isOutputStrInstr;
    }

    public boolean isConStrHead() {
        return isConStrHead;
    }

    public int getValForOutput() {
        return val;
    }

    public ArrayList<Value> getParam() {
        return operands;
    }

    public void setName() {
        if (type instanceof Integer32Type) this.name = IRController.getInstance().genVirtualRegNum();
    }

    public void setConStrName(String name) {
        ConstrName = name;
    }

    @Override
    public void genMIPS() {
        ArrayList<Value> params = new ArrayList<>(this.operands);
        CommentAsm asm = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(asm);
        if (isOutputInstr) {
            PushForSyscall();
            if (isOutputStrInstr && isConStrHead) {
                LaAsm la = new LaAsm(Register.A0, new LabelAsm(ConstrName));
                MipsController.getInstance().addAsm(la);
                LiAsm li = new LiAsm(Register.V0, 4);
                MipsController.getInstance().addAsm(li);
                SyscallAsm syscall = new SyscallAsm();
                MipsController.getInstance().addAsm(syscall);
            } else {
                Value v = params.get(0);
                if (v.isUseReg()) {
                    MoveAsm moveAsm = new MoveAsm(Register.A0, v.getRegister());
                    MipsController.getInstance().addAsm(moveAsm);
                    LiAsm li = new LiAsm(Register.V0, 1);
                    MipsController.getInstance().addAsm(li);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsController.getInstance().addAsm(syscall);
                } else if (v instanceof ConstInteger constInteger) {
                    LiAsm li = new LiAsm(Register.A0, constInteger.getVal());
                    MipsController.getInstance().addAsm(li);
                    LiAsm li1 = new LiAsm(Register.V0, 1);
                    MipsController.getInstance().addAsm(li1);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsController.getInstance().addAsm(syscall);
                } else {
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.A0, Register.SP, v.getOffset());
                    MipsController.getInstance().addAsm(lw);
                    LiAsm li = new LiAsm(Register.V0, 1);
                    MipsController.getInstance().addAsm(li);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsController.getInstance().addAsm(syscall);
                }
            }
            PopForSyscall();
        } else if (isInputInstr) {
            LiAsm li = new LiAsm(Register.V0, 5);
            MipsController.getInstance().addAsm(li);
            SyscallAsm syscall = new SyscallAsm();
            MipsController.getInstance().addAsm(syscall);
            RegDispatcher.getInstance().distributeRegFor(this);
            if (useReg) {
                MoveAsm move = new MoveAsm(this.register, Register.V0);
                MipsController.getInstance().addAsm(move);
            } else {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.V0, Register.SP, offset);
                MipsController.getInstance().addAsm(sw);
            }
        } else {
            if (!(type instanceof VoidType)) RegDispatcher.getInstance().distributeRegFor(this);
            PushMarco pushMarco = new PushMarco(RegDispatcher.getInstance().RegToPushInStack());
            int off = RegDispatcher.getInstance().getCurrentOffset();
            LinkedHashSet<Register> registers = Register.argsRegs();
            int extraOff = RegDispatcher.getInstance().getCurrentOffset() - RegDispatcher.getInstance().getUsedRegsSize();
            for (Value v : params) {
                if (registers.isEmpty()) {
                    if (v.isUseReg()) {
                        extraOff -= 4;
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, v.getRegister(), Register.SP, extraOff);
                        MipsController.getInstance().addAsm(sw);
                    } else if (v instanceof ConstInteger constInteger) {
                        extraOff -= 4;
                        LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
                        MipsController.getInstance().addAsm(li);
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, extraOff);
                        MipsController.getInstance().addAsm(sw);
                    } else {
                        extraOff -= 4;
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, v.getOffset());
                        MipsController.getInstance().addAsm(lw);
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, extraOff);
                        MipsController.getInstance().addAsm(sw);
                    }
                } else {
                    if (v.isUseReg()) {
                        MoveAsm move = new MoveAsm(registers.iterator().next(), v.getRegister());
                        MipsController.getInstance().addAsm(move);
                    } else if (v instanceof ConstInteger constInteger) {
                        LiAsm li = new LiAsm(registers.iterator().next(), constInteger.getVal());
                        MipsController.getInstance().addAsm(li);
                    } else {
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, registers.iterator().next(), Register.SP, v.getOffset());
                        MipsController.getInstance().addAsm(lw);
                    }
                    registers.remove(registers.iterator().next());
                }
            }
            pushMarco.addToMipsController();
            //TODO
            JalAsm jal = new JalAsm(new LabelAsm("func_" + functionName.substring(1)));
            MipsController.getInstance().addAsm(jal);
            PopMarco popMarco = new PopMarco(RegDispatcher.getInstance().RegToPushInStack(), off);
            popMarco.addToMipsController();
            if (!(type instanceof VoidType)) {
                if (useReg) {
                    MoveAsm move = new MoveAsm(this.register, Register.V0);
                    MipsController.getInstance().addAsm(move);
                } else {
                    MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.V0, Register.SP, offset);
                    MipsController.getInstance().addAsm(sw);
                }
            }
        }
    }

    private void PushForSyscall() {
        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.A0, Register.SP, RegDispatcher.getInstance().getCurrentOffset() - 4);
        MipsController.getInstance().addAsm(sw);
    }

    private void PopForSyscall() {
        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.A0, Register.SP, RegDispatcher.getInstance().getCurrentOffset() - 4);
        MipsController.getInstance().addAsm(lw);
    }
}
