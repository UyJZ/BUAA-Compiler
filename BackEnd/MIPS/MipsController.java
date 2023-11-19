package BackEnd.MIPS;

import BackEnd.MIPS.Assembly.Asm;
import BackEnd.MIPS.Assembly.BlockAsm;
import BackEnd.MIPS.Assembly.Data;
import BackEnd.MIPS.Assembly.FunctionAsm;
import llvm_ir.IRController;
import llvm_ir.Module;
import llvm_ir.Values.BasicBlock;
import llvm_ir.Values.Function;

import java.util.ArrayList;

public class MipsController {
    private static final MipsController mipsController = new MipsController();

    private ArrayList<FunctionAsm> textSegment;

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
        //run mips
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
        FunctionAsm functionAsm = new FunctionAsm("func_" + function.getName());
        textSegment.add(functionAsm);
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
        currentBlockAsm.addAsm(asm);
    }

    public void addGlobalVar(Data data) {
        dataSegment.add(data);
    }
}
