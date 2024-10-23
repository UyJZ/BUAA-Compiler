package Ir_LLVM.LLVM_Values.Instr;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsBuilder;
import BackEnd.MIPS.PopMarco;
import BackEnd.MIPS.PushMarco;
import BackEnd.MIPS.Register;
import Config.Tasks;
import Ir_LLVM.LLVM_Value;
import MidEnd.RegDispatcher;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.ConstInteger;
import Ir_LLVM.LLVM_Values.Function;
import Ir_LLVM.LLVM_Types.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    private boolean hasPointer;

    private int val;

    public CallInstr(LLVMType type, LLVM_Value func, ArrayList<LLVM_Value> params) {
        //库函数
        super(type, type instanceof VoidType || Tasks.isIsSetNameAfterGen() ? "" : LLVM_Builder.getInstance().genVirtualRegNum());
        functionName = func.getName();
        isIOInstr = functionName.equals("@getint") || functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        isInputInstr = functionName.equals("@getint");
        isOutputInstr = functionName.equals("@putint") || functionName.equals("@putch") || functionName.equals("@putstr");
        isOutputStrInstr = functionName.equals("@putstr") || functionName.equals("@putch");
        for (LLVM_Value v : params) this.addValue(v);
        if (functionName.equals("@putch")) {
            val = ((ConstInteger) params.get(0)).getVal();
        }
        if (!isIOInstr)
            LLVM_Builder.getInstance().getCurrentFunction().addCalledFunc((Function) func);
        hasPointer = false;
        for (LLVM_Value v : params) {
            if (v.getType() instanceof PointerType || v.getType() instanceof ArrayType) {
                hasPointer = true;
                break;
            }
        }
        function = (Function) func;
    }

    public Function getFunction() {
        return function;
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

    public ArrayList<LLVM_Value> getParam() {
        return operands;
    }

    public void setName() {
        if (type instanceof Integer32Type) this.name = LLVM_Builder.getInstance().genVirtualRegNum();
    }

    public void clearName() {
        if (type instanceof Integer32Type) this.name = "";
    }

    public void setConStrName(String name) {
        ConstrName = name;
    }

    @Override
    public void genMIPS() {
        ArrayList<LLVM_Value> params = new ArrayList<>(this.operands);
        CommentAsm asm = new CommentAsm(this.toString());
        MipsBuilder.getInstance().addAsm(asm);
        if (isOutputInstr) {
            PushForSyscall();
            if (isOutputStrInstr && isConStrHead) {
                LaAsm la = new LaAsm(Register.A0, new LabelAsm(ConstrName));
                MipsBuilder.getInstance().addAsm(la);
                LiAsm li = new LiAsm(Register.V0, 4);
                MipsBuilder.getInstance().addAsm(li);
                SyscallAsm syscall = new SyscallAsm();
                MipsBuilder.getInstance().addAsm(syscall);
            } else {
                LLVM_Value v = params.get(0);
                if (v.isUseReg()) {
                    MoveAsm moveAsm = new MoveAsm(Register.A0, v.getRegister());
                    MipsBuilder.getInstance().addAsm(moveAsm);
                    LiAsm li = new LiAsm(Register.V0, 1);
                    MipsBuilder.getInstance().addAsm(li);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsBuilder.getInstance().addAsm(syscall);
                } else if (v instanceof ConstInteger constInteger) {
                    LiAsm li = new LiAsm(Register.A0, constInteger.getVal());
                    MipsBuilder.getInstance().addAsm(li);
                    LiAsm li1 = new LiAsm(Register.V0, 1);
                    MipsBuilder.getInstance().addAsm(li1);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsBuilder.getInstance().addAsm(syscall);
                } else {
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.A0, Register.SP, v.getOffset());
                    MipsBuilder.getInstance().addAsm(lw);
                    LiAsm li = new LiAsm(Register.V0, 1);
                    MipsBuilder.getInstance().addAsm(li);
                    SyscallAsm syscall = new SyscallAsm();
                    MipsBuilder.getInstance().addAsm(syscall);
                }
            }
            PopForSyscall();
        } else if (isInputInstr) {
            LiAsm li = new LiAsm(Register.V0, 5);
            MipsBuilder.getInstance().addAsm(li);
            SyscallAsm syscall = new SyscallAsm();
            MipsBuilder.getInstance().addAsm(syscall);
            RegDispatcher.getInstance().distributeRegFor(this);
            if (useReg) {
                MoveAsm move = new MoveAsm(this.register, Register.V0);
                MipsBuilder.getInstance().addAsm(move);
            } else {
                MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.V0, Register.SP, offset);
                MipsBuilder.getInstance().addAsm(sw);
            }
        } else {
            if (!(type instanceof VoidType)) RegDispatcher.getInstance().distributeRegFor(this);
            PushMarco pushMarco_0 = new PushMarco(RegDispatcher.getInstance().usedRegister());
            PushMarco pushMarco = new PushMarco(RegDispatcher.getInstance().systemReg());
            LinkedHashSet<Register> regToPush = new LinkedHashSet<>(RegDispatcher.getInstance().usedRegister());
            HashMap<Register, Integer> offMap = new HashMap<>();
            int len = 1;
            for (Register register : regToPush) {
                offMap.put(register, RegDispatcher.getInstance().getCurrentOffset() - 4 * len);
                len++;
            }
            int off = RegDispatcher.getInstance().getCurrentOffset();
            pushMarco_0.addToMipsControllerOnlyForNormalReg();
            LinkedHashSet<Register> registers = Register.argsRegs();
            int extraOff = RegDispatcher.getInstance().getCurrentOffset() - RegDispatcher.getInstance().systemReg().size() * 4;
            for (LLVM_Value v : params) {
                if (registers.isEmpty()) {
                    if (v.isUseReg() && registers.contains(v.getRegister())) {
                        extraOff -= 4;
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, v.getRegister(), Register.SP, extraOff);
                        MipsBuilder.getInstance().addAsm(sw);
                    } else if (v instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                        extraOff -= 4;
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.ZERO, Register.SP, extraOff);
                        MipsBuilder.getInstance().addAsm(sw);
                    } else if (v.isUseReg() && !registers.contains(v.getRegister())) {
                        extraOff -= 4;
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, offMap.get(v.getRegister()));
                        MipsBuilder.getInstance().addAsm(lw);
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, extraOff);
                        MipsBuilder.getInstance().addAsm(sw);
                    } else if (v instanceof ConstInteger constInteger) {
                        extraOff -= 4;
                        LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
                        MipsBuilder.getInstance().addAsm(li);
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, extraOff);
                        MipsBuilder.getInstance().addAsm(sw);
                    } else {
                        extraOff -= 4;
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, v.getOffset());
                        MipsBuilder.getInstance().addAsm(lw);
                        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.K0, Register.SP, extraOff);
                        MipsBuilder.getInstance().addAsm(sw);
                    }
                } else {
                    if (v.isUseReg() && registers.contains(v.getRegister())) {
                        MoveAsm move = new MoveAsm(registers.iterator().next(), v.getRegister());
                        MipsBuilder.getInstance().addAsm(move);
                    } else if (v instanceof ConstInteger constInteger && constInteger.getVal() == 0) {
                        MoveAsm move = new MoveAsm(registers.iterator().next(), Register.ZERO);
                        MipsBuilder.getInstance().addAsm(move);
                    } else if (v.isUseReg() && !registers.contains(v.getRegister())) {
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, registers.iterator().next(), Register.SP, offMap.get(v.getRegister()));
                        MipsBuilder.getInstance().addAsm(lw);
                    } else if (v instanceof ConstInteger constInteger) {
                        LiAsm li = new LiAsm(registers.iterator().next(), constInteger.getVal());
                        MipsBuilder.getInstance().addAsm(li);
                    } else {
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, registers.iterator().next(), Register.SP, v.getOffset());
                        MipsBuilder.getInstance().addAsm(lw);
                    }
                    registers.remove(registers.iterator().next());
                }
            }
            pushMarco.addToMipsControllerForSysReg();
            //TODO
            JalAsm jal = new JalAsm(new LabelAsm("func_" + functionName.substring(1)));
            MipsBuilder.getInstance().addAsm(jal);
            LinkedHashSet<Register> regToPop = new LinkedHashSet<>(RegDispatcher.getInstance().usedRegister());
            regToPop.addAll(RegDispatcher.getInstance().systemReg());
            PopMarco popMarco = new PopMarco(regToPop);
            popMarco.addToMipsController();
            if (!(type instanceof VoidType)) {
                if (useReg) {
                    MoveAsm move = new MoveAsm(this.register, Register.V0);
                    MipsBuilder.getInstance().addAsm(move);
                } else {
                    MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.V0, Register.SP, offset);
                    MipsBuilder.getInstance().addAsm(sw);
                }
            }
        }
    }

    private void PushForSyscall() {
        MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, Register.A0, Register.SP, RegDispatcher.getInstance().getCurrentOffset() - 4);
        MipsBuilder.getInstance().addAsm(sw);
    }

    private void PopForSyscall() {
        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.A0, Register.SP, RegDispatcher.getInstance().getCurrentOffset() - 4);
        MipsBuilder.getInstance().addAsm(lw);
    }

    @Override
    public Instr copy(HashMap<LLVM_Value, LLVM_Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        ArrayList<LLVM_Value> params = new ArrayList<>();
        for (LLVM_Value v : operands) {
            params.add(v.copy(map));
        }
        return new CallInstr(type, function, params);
    }

    @Override
    public boolean isDefinition() {
        return type instanceof Integer32Type;
    }

    @Override
    public String GVNHash() {
        assert !function.hasSideEffect();
        StringBuilder sb = new StringBuilder();
        sb.append("call_").append(function.getName()).append("(");
        for (int i = 0;i < operands.size(); i++) {
            sb.append(operands.get(i).hash);
            if (i != operands.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
