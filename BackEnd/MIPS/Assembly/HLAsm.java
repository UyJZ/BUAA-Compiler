package BackEnd.MIPS.Assembly;

import BackEnd.MIPS.Register;

public class HLAsm extends Asm {

    public enum Op {
        mfhi, mflo, mthi, mtlo
    }

    private Op op;

    private Register rs;

}
