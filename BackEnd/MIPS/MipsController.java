package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.Asm;
import BackEnd.MIPS.Assembly.BlockAsm;
import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.FunctionAsm;
import llvm_ir.IRController;
import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;
import llvm_ir.llvmType.PointerType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MipsController {
    private static final MipsController mipsController = new MipsController();

    private ArrayList<Asm> textSegment;

    private ArrayList<Data> dataSegment;

    private Module module;

    private Function currentFunction = null;

    private BlockAsm currentBlockAsm;

    private FunctionAsm currentFuncAsm;

    private int currentConStrNum = 0;

    private ArrayList<FunctionAsm> functionArrayList;

    public static MipsController getInstance() {
        return mipsController;
    }

    public void run() {
        module.genMIPS();
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

    public void setModule(Module module) {
        this.module = module;
    }


    public MipsController() {
        this.module = IRController.getInstance().getModule();
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
}
