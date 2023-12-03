package MidEnd;

import llvm_ir.Module;

public class Optimizer {
    private final Module module;

    private final SSABuilder ssaBuilder;

    private final CFGBuilder cfgBuilder;

    private final DeadCodeDeletion deadCodeDeletion;

    private final FuncInline funcInline;

    private final GVN_GCM gvnGcm;

    private final MergeBlock mergeBlock;

    private final De_SSA deSsa;

    public Optimizer(Module module) {
        this.module = module;
        this.ssaBuilder = new SSABuilder(module);
        this.cfgBuilder = new CFGBuilder(module);
        this.deadCodeDeletion = new DeadCodeDeletion(module);
        this.funcInline = new FuncInline(module);
        this.gvnGcm = new GVN_GCM(module);
        this.mergeBlock = new MergeBlock(module);
        this.deSsa = new De_SSA(module);
    }

    public void run() {
        cfgBuilder.run();
        ssaBuilder.run();
        GlobalForInline globalForInline = new GlobalForInline();
        globalForInline.setModule(module);
        //funcInline.run();
        deadCodeDeletion.run();
        cfgBuilder.run();
        gvnGcm.run();
        cfgBuilder.run();
        deSsa.run();
        cfgBuilder.run();
        mergeBlock.run();
        cfgBuilder.run();
        module.setName();
    }
}
