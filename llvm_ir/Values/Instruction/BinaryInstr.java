package llvm_ir.Values.Instruction;

import BackEnd.MIPS.Assembly.*;
import BackEnd.MIPS.MipsController;
import BackEnd.MIPS.Register;
import MidEnd.RegDispatcher;
import llvm_ir.IRController;
import llvm_ir.Value;
import llvm_ir.Values.ConstInteger;
import llvm_ir.llvmType.LLVMType;
import Config.tasks;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class BinaryInstr extends Instr {

    public enum op {
        ADD, SUB, SREM, MUL, SDIV, AND, OR
    }

    private op opcode;

    public BinaryInstr(LLVMType type, Value oprand1, Value oprand2, op opcode) {
        super(type, tasks.isSetNameAfterGen ? "" : IRController.getInstance().genVirtualRegNum()); //TODO:nameGen
        this.opcode = opcode;
        this.addValue(oprand1);
        this.addValue(oprand2);
    }

    public BinaryInstr(LLVMType type, Value operand, op opcode) { //for -x or +x
        super(type, tasks.isSetNameAfterGen ? "" : IRController.getInstance().genVirtualRegNum());
        this.opcode = opcode;
        this.addValue(new ConstInteger(0));
        this.addValue(operand);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ");
        switch (opcode) {
            case ADD -> sb.append("add ");
            case SUB -> sb.append("sub ");
            case SREM -> sb.append("srem ");
            case MUL -> sb.append("mul ");
            case SDIV -> sb.append("sdiv ");
            case AND -> sb.append("and ");
            case OR -> sb.append("or ");
        }
        if (operands.size() == 2)
            sb.append(type.toString()).append(" ").append(operands.get(0).getName()).append(", ").append(operands.get(1).getName());
        else
            sb.append(type.toString()).append(" ").append("0 ").append(", ").append(operands.get(0).getName());
        return sb.toString();
    }

    @Override
    public void genMIPS() {
        CommentAsm asm0 = new CommentAsm(this.toString());
        MipsController.getInstance().addAsm(asm0);
        //结果先统一存在K0里面
        RegDispatcher.getInstance().distributeRegFor(this);
        Register tar = useReg ? register : Register.K0;
        Value operand1 = operands.get(0);
        Value operand2 = operands.get(1);
        //this is for addi but op1 & op2 can't be constInteger at the same time
        HashSet<op> ALU = new HashSet<>();
        ALU.add(op.ADD);
        ALU.add(op.SUB);
        ALU.add(op.OR);
        ALU.add(op.AND);
        if (ALU.contains(opcode)) {
            if ((operand1 instanceof ConstInteger constInteger) && (operand2 instanceof ConstInteger constInteger1)) {
                int val = 0;
                switch (opcode) {
                    case ADD -> val = constInteger.getVal() + constInteger1.getVal();
                    case SUB -> val = constInteger.getVal() - constInteger1.getVal();
                    case AND -> val = constInteger.getVal() & constInteger1.getVal();
                    case OR -> val = constInteger.getVal() | constInteger1.getVal();
                }
                LiAsm li = new LiAsm(tar, val);
                MipsController.getInstance().addAsm(li);
            } else if (operand1 instanceof ConstInteger constInteger && operand2.isUseReg() && useReg && register == operand2.getRegister() && constInteger.getVal() == 0 && (opcode == op.ADD)) {
                return;
            } else if (operand2 instanceof ConstInteger constInteger && operand1.isUseReg() && useReg && register == operand1.getRegister() && constInteger.getVal() == 0 && (opcode == op.ADD || opcode == op.SUB)) {
                return;
            }

            else if ((operand1 instanceof ConstInteger constInteger && constInteger.getVal() != 0 && !AluITAsm.isOutOfRange(constInteger.getVal())) ||
                    (operand2 instanceof ConstInteger constInteger1 && constInteger1.getVal() != 0 && !AluITAsm.isOutOfRange(constInteger1.getVal()))) {
                AluITAsm.Op aluOp = null;
                switch (opcode) {
                    case ADD -> aluOp = AluITAsm.Op.addiu;
                    case SUB -> aluOp = AluITAsm.Op.subiu;
                    case AND -> aluOp = AluITAsm.Op.andi;
                    case OR -> aluOp = AluITAsm.Op.ori;
                }
                if (operand1 instanceof ConstInteger constInteger) {
                    if (operand2.isUseReg()) {
                        AluITAsm aluITAsm = new AluITAsm(aluOp, tar, operand2.getRegister(), constInteger.getVal());
                        MipsController.getInstance().addAsm(aluITAsm);
                    } else {
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand2.getOffset());
                        MipsController.getInstance().addAsm(lw);
                        AluITAsm aluITAsm = new AluITAsm(aluOp, tar, Register.K0, constInteger.getVal());
                        MipsController.getInstance().addAsm(aluITAsm);
                    }
                    if (aluOp == AluITAsm.Op.subiu) {
                        AluRTAsm asm = new AluRTAsm(AluRTAsm.Op.subu, tar, Register.ZERO, tar);
                        MipsController.getInstance().addAsm(asm);
                    }
                } else {
                    if (operand1.isUseReg()) {
                        AluITAsm aluITAsm = new AluITAsm(aluOp, tar, operand1.getRegister(), ((ConstInteger) operand2).getVal());
                        MipsController.getInstance().addAsm(aluITAsm);
                    } else {
                        MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand1.getOffset());
                        MipsController.getInstance().addAsm(lw);
                        AluITAsm aluITAsm = new AluITAsm(aluOp, tar, Register.K0, ((ConstInteger) operand2).getVal());
                        MipsController.getInstance().addAsm(aluITAsm);
                    }
                }
            }

            else {
                AluRTAsm.Op aluOp = null;
                switch (opcode) {
                    case ADD -> aluOp = AluRTAsm.Op.addu;
                    case SUB -> aluOp = AluRTAsm.Op.subu;
                    case AND -> aluOp = AluRTAsm.Op.and;
                    case OR -> aluOp = AluRTAsm.Op.or;
                }
                Register r1;
                Register r2;
                if (operand1.isUseReg()) {
                    r1 = operand1.getRegister();
                } else if (operand1 instanceof ConstInteger constInteger) {
                    LiAsm li = new LiAsm(Register.K0, constInteger.getVal());
                    MipsController.getInstance().addAsm(li);
                    r1 = Register.K0;
                } else {
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand1.getOffset());
                    MipsController.getInstance().addAsm(lw);
                    r1 = Register.K0;
                }
                if (operand2.isUseReg()) {
                    r2 = operand2.getRegister();
                } else if (operand2 instanceof ConstInteger constInteger) {
                    LiAsm li = new LiAsm(Register.K1, constInteger.getVal());
                    MipsController.getInstance().addAsm(li);
                    r2 = Register.K1;
                } else {
                    MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, operand2.getOffset());
                    MipsController.getInstance().addAsm(lw);
                    r2 = Register.K1;
                }
                AluRTAsm asm = new AluRTAsm(aluOp, tar, r1, r2);
                MipsController.getInstance().addAsm(asm);
            }
        } else {
            MulDivAsm.Op op = null;
            switch (opcode) {
                case MUL -> op = MulDivAsm.Op.mult;
                case SDIV, SREM -> op = MulDivAsm.Op.div;
            }
            Register r1;
            Register r2;
            if (false && opcode == BinaryInstr.op.SDIV && tasks.isOptimize && operand2 instanceof ConstInteger constInteger && !(operand1 instanceof ConstInteger)) {
                //TODO
                 boolean flag = optimizeDiv(operand1, constInteger.getVal());
                 if (flag) return;
            }
            if (operand1 instanceof ConstInteger constInteger && operand2 instanceof ConstInteger constInteger1) {
                int val = 0;
                switch (opcode) {
                    case MUL -> val = constInteger.getVal() * constInteger1.getVal();
                    case SDIV -> val = constInteger.getVal() / constInteger1.getVal();
                    case SREM -> val = constInteger.getVal() % constInteger1.getVal();
                }
                LiAsm li = new LiAsm(tar, val);
                MipsController.getInstance().addAsm(li);
                if (!useReg) {
                    MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, tar, Register.SP, this.offset);
                    MipsController.getInstance().addAsm(sw);
                }
                return;
            }
            if (operand1 instanceof ConstInteger) {
                LiAsm li = new LiAsm(Register.K0, ((ConstInteger) operand1).getVal());
                MipsController.getInstance().addAsm(li);
                r1 = Register.K0;
            } else if (operand1.isUseReg()) {
                r1 = operand1.getRegister();
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K0, Register.SP, operand1.getOffset());
                MipsController.getInstance().addAsm(lw);
                r1 = Register.K0;
            }
            if (operand2 instanceof ConstInteger) {
                LiAsm li = new LiAsm(Register.K1, ((ConstInteger) operand2).getVal());
                MipsController.getInstance().addAsm(li);
                r2 = Register.K1;
            } else if (operand2.isUseReg()) {
                r2 = operand2.getRegister();
            } else {
                MemITAsm lw = new MemITAsm(MemITAsm.Op.lw, Register.K1, Register.SP, operand2.getOffset());
                MipsController.getInstance().addAsm(lw);
                r2 = Register.K1;
            }
            MulDivAsm mulDivAsm = new MulDivAsm(op, r1, r2);
            MipsController.getInstance().addAsm(mulDivAsm);
            if (opcode == BinaryInstr.op.SREM) {
                HLAsm mfhi = new HLAsm(HLAsm.Op.mfhi, tar);
                MipsController.getInstance().addAsm(mfhi);
            } else {
                HLAsm mflo = new HLAsm(HLAsm.Op.mflo, tar);
                MipsController.getInstance().addAsm(mflo);
            }
        }
        if (!useReg) {
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, tar, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
        }
    }

    @Override
    public Instr copy(HashMap<Value, Value> map) {
        if (map.containsKey(this)) return (Instr) map.get(this);
        return new BinaryInstr(type, operands.get(0).copy(map), operands.get(1).copy(map), opcode);
    }

    @Override
    public String GVNHash() {
        StringBuilder sb = new StringBuilder();
        if (opcode == op.MUL || opcode == op.OR || opcode == op.ADD || opcode == op.AND) {
            if (opcode == op.MUL && operands.get(0) instanceof ConstInteger constInteger && constInteger.getVal() == 2) {
                sb.append(operands.get(1).hash).append(" ").append(op.ADD).append(" ").append(operands.get(1).hash);
            } else if (operands.get(0).hash.compareTo(operands.get(1).hash) < 0) {
                sb.append(operands.get(0).hash).append(" ").append(opcode).append(" ").append(operands.get(1).hash);
            } else {
                sb.append(operands.get(1).hash).append(" ").append(opcode).append(" ").append(operands.get(0).hash);
            }
        } else {
            sb.append(operands.get(0).hash).append(" ").append(opcode).append(" ").append(operands.get(1).hash);
        }
        return sb.toString();
    }

    private boolean optimizeDiv(Value v, int val) {
        BigInteger d = new BigInteger(String.valueOf(val));
        BigInteger N = new BigInteger(String.valueOf(32));
        BigInteger m = new BigInteger(String.valueOf(0));
        int l = 0;
        for (l = 0; l < 32; l++) {
            BigInteger up = BigInteger.ONE.shiftLeft(l).add(BigInteger.ONE.shiftLeft(l + 32));
            BigInteger down = BigInteger.ONE.shiftLeft(l + 32);
            BigInteger upRes = up.divide(d);
            BigInteger downRes = down.divide(d);
            if (!upRes.equals(downRes)) {
                m = (upRes.add(downRes)).divide(BigInteger.valueOf(2));
                break;
            }
        }
        if (m.subtract(new BigInteger(String.valueOf(Integer.MAX_VALUE / 2))).compareTo(new BigInteger(String.valueOf(0))) > 0) {
            return false;
        }
        LiAsm li = new LiAsm(Register.K0, m.intValue());
        MipsController.getInstance().addAsm(li);
        MulDivAsm mul = new MulDivAsm(MulDivAsm.Op.mult, v.getRegister(), Register.K0);
        MipsController.getInstance().addAsm(mul);
        HLAsm mfhi = new HLAsm(HLAsm.Op.mfhi, Register.K1);
        MipsController.getInstance().addAsm(mfhi);
        Register tar;
        if (this.useReg) {
            tar = this.register;
        } else {
            tar = Register.K0;
        }
        AluITAsm srl = new AluITAsm(AluITAsm.Op.srl, tar, Register.K1, l);
        MipsController.getInstance().addAsm(srl);
        if (!this.useReg) {
            MemITAsm sw = new MemITAsm(MemITAsm.Op.sw, tar, Register.SP, this.offset);
            MipsController.getInstance().addAsm(sw);
        }
        return true;
    }

    public op getOpcode() {
        return opcode;
    }

    @Override
    public boolean isDefinition() {
        return true;
    }
}
