package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.Asm;
import BackEnd.MIPS.Assembly.BlockAsm;
import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.FunctionAsm;
import Ir_LLVM.LLVM_Module;
import Ir_LLVM.LLVM_Builder;
import Ir_LLVM.LLVM_Values.BasicBlock;
import Ir_LLVM.LLVM_Values.Function;

import java.io.PrintStream;
import java.util.ArrayList;

public class MipsBuilder {
    private static final MipsBuilder MIPS_BUILDER = new MipsBuilder();

    private ArrayList<Asm> textSegment;

    private ArrayList<Data> dataSegment;

    private LLVM_Module LLVMModule;

    private Function currentFunction = null;

    private BlockAsm currentBlockAsm;

    private FunctionAsm currentFuncAsm;

    private BasicBlock block;

    private int currentConStrNum = 0;

    private ArrayList<FunctionAsm> functionArrayList;

    public static MipsBuilder getInstance() {
        return MIPS_BUILDER;
    }

    public void run() {
        LLVMModule.genMIPS();
        //run mips
    }

    public void print(PrintStream ps) {
        ps.println(".data");
        for (Data data : dataSegment) {
            ps.println(data);
        }
        ps.println(".text");
        for (Asm functionAsm : textSegment) {
            ps.println(functionAsm);
        }
    }

    public void setModule(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
    }


    public MipsBuilder() {
        this.LLVMModule = LLVM_Builder.getInstance().getModule();
        functionArrayList = new ArrayList<>();
        textSegment = new ArrayList<>();
        dataSegment = new ArrayList<>();
    }

    public FunctionAsm getCurrentFunctionAsm() {
        return functionArrayList.get(functionArrayList.size() - 1);
    }

    public void addFunction(Function function) {
        currentFunction = function;
        FunctionAsm functionAsm = new FunctionAsm("func_" + function.getName().substring(1));
        textSegment.add(functionAsm);
        currentFuncAsm = functionAsm;
        functionArrayList.add(functionAsm);
        //add function to mips
    }

    public String genConStrName() {
        return "str_" + String.valueOf(currentConStrNum++);
        //add con str to mips
    }

    public void addBasicBlock(BasicBlock block) {
        currentBlockAsm = new BlockAsm(currentFuncAsm, block);
        currentFuncAsm.addBasicBlock(currentBlockAsm);
        this.block = block;
    }

    public void addAsm(Asm asm) {
        if (currentBlockAsm != null)
            currentBlockAsm.addAsm(asm);
        else textSegment.add(asm);
    }

    public void addEntry(Asm asm) {
        textSegment.add(asm);
    }

    public void addGlobalVar(Data data) {
        dataSegment.add(data);
    }

    public Function getCurrentFunction() {
        return currentFunction;
    }

    public BasicBlock getCurrentBlock() {
        return block;
    }
}
