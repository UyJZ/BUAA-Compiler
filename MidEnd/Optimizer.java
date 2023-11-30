package MidEnd;

import llvm_ir.Module;

public class Optimizer {
    private final Module module;

    private final SSABuilder ssaBuilder;

    private final CFGBuilder cfgBuilder;

    private final DeadCodeDeletion deadCodeDeletion;

    private final FuncInline funcInline;

    public Optimizer(Module module) {
        this.module = module;
        this.ssaBuilder = new SSABuilder(module);
        this.cfgBuilder = new CFGBuilder(module);
        this.deadCodeDeletion = new DeadCodeDeletion(module);
        this.funcInline = new FuncInline(module);
    }

    public void run() {
        cfgBuilder.run();
        ssaBuilder.run();
        GlobalForInline globalForInline = new GlobalForInline();
        globalForInline.setModule(module);
        funcInline.run();
        deadCodeDeletion.run();
    }
}
