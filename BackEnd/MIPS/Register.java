package BackEnd.MIPS;

import java.util.LinkedHashSet;

public enum Register {
    ZERO("$zero"),
    V0("$v0"),
    V1("$v1"),
    A0("$a0"),
    A1("$a1"),
    A2("$a2"),
    A3("$a3"),
    T0("$t0"),
    T1("$t1"),
    T2("$t2"),
    T3("$t3"),
    T4("$t4"),
    T5("$t5"),
    T6("$t6"),
    T7("$t7"),
    S0("$s0"),
    S1("$s1"),
    S2("$s2"),
    S3("$s3"),
    S4("$s4"),
    S5("$s5"),
    S6("$s6"),
    S7("$s7"),
    T8("$t8"),
    T9("$t9"),
    K0("$k0"),
    K1("$k1"),
    GP("$gp"),
    SP("$sp"),
    FP("$fp"),
    RA("$ra");

    private final String name;

    Register(String name) {
        this.name = name;
    }

    public static Register indexToReg(int index) {
        return values()[index];
    }

    public static Register nameToReg(String name) {
        for (Register reg : values()) {
            if (reg.toString().equals(name)) {
                return reg;
            }
        }
        return null;
    }

    public static LinkedHashSet<Register> tempRegs() {
        LinkedHashSet<Register> set = new LinkedHashSet<>();
        for (int i = T0.ordinal(); i <= T9.ordinal(); i++) {
            set.add(indexToReg(i));
        }
        return set;
    }

    public static LinkedHashSet<Register> argsRegs() {
        LinkedHashSet<Register> set = new LinkedHashSet<>();
        for (int i = A0.ordinal(); i <= A3.ordinal(); i++) {
            set.add(indexToReg(i));
        }
        return set;
    }

    public static int regToIndex(Register reg) {
        return reg.ordinal();
    }

    @Override
    public String toString() {
        return name;
    }
}
