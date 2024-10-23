package MidEnd;

import IR_LLVM.LLVM_Module;

public class Optimizer {
    private final LLVM_Module LLVMModule;

    private final SSABuilder ssaBuilder;

    private final CFGBuilder cfgBuilder;

    private final DeadCodeDeletion deadCodeDeletion;

    private final FuncInline funcInline;

    private final GVN_GCM gvnGcm;

    private final MergeBlock mergeBlock;

    private final ActAnalysis actAnalysis;

    private final RegAllocator regAllocator;

    private final RegAllocatorForSSA regAllocatorForSSA;

    private final De_SSA deSsa;

    public Optimizer(LLVM_Module LLVMModule) {
        this.LLVMModule = LLVMModule;
        this.ssaBuilder = new SSABuilder(LLVMModule);
        this.cfgBuilder = new CFGBuilder(LLVMModule);
        this.deadCodeDeletion = new DeadCodeDeletion(LLVMModule);
        this.funcInline = new FuncInline(LLVMModule);
        this.gvnGcm = new GVN_GCM(LLVMModule);
        this.mergeBlock = new MergeBlock(LLVMModule);
        this.deSsa = new De_SSA(LLVMModule);
        this.actAnalysis = new ActAnalysis(LLVMModule);
        this.regAllocator = new RegAllocator(LLVMModule);
        this.regAllocatorForSSA = new RegAllocatorForSSA(LLVMModule);
    }

    public void run() {
        cfgBuilder.run();
        ssaBuilder.run();
        GlobalForInline globalForInline = new GlobalForInline();
        globalForInline.setModule(LLVMModule);
        gvnGcm.run();
        deadCodeDeletion.run();
        funcInline.run();
        deadCodeDeletion.run();
        cfgBuilder.run();
        gvnGcm.run();
        cfgBuilder.run();
        mergeBlock.run();
        actAnalysis.run();
        cfgBuilder.run();
        regAllocatorForSSA.run();
        deSsa.run();
        cfgBuilder.run();
        mergeBlock.run();
        cfgBuilder.run();
        LLVMModule.setName();
        //regAllocator.run();
    }
}
