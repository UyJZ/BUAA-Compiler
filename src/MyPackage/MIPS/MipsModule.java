package MyPackage.MIPS;

import MyPackage.IR.Instruction.GetelementptrLlvm;
import MyPackage.IR.Instruction.IcmpLlvm;
import MyPackage.IR.Value;
import MyPackage.MIPS.Instruction.Lw;
import MyPackage.MIPS.Instruction.Sw;
import MyPackage.OutPut;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsModule {
    static ArrayList<MipsDecl> data = new ArrayList<>();
    static ArrayList<Mips> text = new ArrayList<>();
    static int stringCount = 0;
    static HashMap<Integer, Integer> address = new HashMap<>();
    static HashMap<Integer, String> reg = new HashMap<>();

    static public HashMap<Integer, String> param = new HashMap<>();
    static public HashMap<Integer, Integer> alloc = new HashMap<>();
    static int stack = 0;
    static int regID = -1;
    public static StringBuilder stringBuilder = null;
    public static boolean isPut = false;

    public static int clearTimes = 0;


    public static void tableClear() {
        address.clear();
        reg.clear();
        param.clear();
        alloc.clear();
        regID = -1;
    }

    public static void addAlloc(int value) {
        alloc.put(value, stack);
    }
    public static void addParam(int value, String re) {
        param.put(value, re);
    }

    public static String getParam(int value) {
        return param.getOrDefault(value, null);
    }

    public static void addAddress(int value, int addr) {
        address.put(value, addr);
    }

    public static int getAddress(int value) {
        return address.getOrDefault(value, -1);
    }

    public static void addDate(MipsDecl mipsDecl) {
        data.add(mipsDecl);
    }

    public static void addText(Mips mips) {
        text.add(mips);
    }

    public static void addString() {
        addDate(new MipsDecl(String.format("lhc_str%d", stringCount), stringBuilder.toString(), "asciiz"));
        stringBuilder = null;
        stringCount++;
    }

    public static boolean isMain = false;

    public static void addReg(int value, String re) {
        if (re.charAt(2) == '9' || re.charAt(2) == '8') {
            int off = MipsModule.getStack();
            MipsModule.addStack(4);
            MipsModule.addText(new Sw(re, "$sp", off));
            MipsModule.addAddress(value, off);
        }
        else {
            reg.put(value, re);
        }
    }

    public static String searchReg(int value) {
        String re = reg.getOrDefault(value, null);
        if (re == null) {
            int off = MipsModule.getAddress(value);
            if (off == -1) {
                re = param.get(value);
            }
            else {
                re = MipsModule.getReg();
                MipsModule.addText(new Lw(re, "$sp", off));
            }
        }
        return re;
    }



    public static String searchReg2(int value) {
        return reg.getOrDefault(value, null);
    }

    public static String searchReg3(int value, String op) {
        String re = reg.getOrDefault(value, null);
        if (re == null) {
            int off = MipsModule.getAddress(value);
            if (off == -1) {
                re = param.get(value);
            }
            else {
                re = op;
                MipsModule.addText(new Lw(re, "$sp", off));
            }
        }
        return re;
    }

    public static void newStack() {
        stack = 0;
    }

    public static int getStack() {
        return stack;
    }

    public static void addStack(int size) {
        stack = stack - size;
    }


    public static void regClear() {
        regID = -1;
        reg.clear();
    }

    public static String getReg() {
        regID++;
        if (regID <= 7) {
            return String.format("$t%d", regID);
        }
        else if (regID <= 15) {
            return String.format("$s%d", regID - 8);
        }
        else if (regID%2 == 1){
            return "$t8";
        }
        else {
            return "$t9";
        }
    }

    public static int getStringCount() {
        return stringCount;
    }

    public static void print() {
        OutPut.printMips(".data");
        for (int i = 0; i < data.size(); i++) {
            data.get(i).print();
        }
        OutPut.printMips(".text");
        for (int i = 0; i < text.size(); i++) {
            text.get(i).print();
        }
    }

    public static void saveReg() {
        for (int value : reg.keySet()) {
            int off = MipsModule.getStack();
            String re = reg.get(value);
            MipsModule.addStack(4);
            MipsModule.addText(new Sw(re, "$sp", off));
            MipsModule.addAddress(value, off);
        }
    }
}
