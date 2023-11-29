package MidEnd;

import llvm_ir.Module;

public class Optimizer {
    private final Module module;

    private final SSABuilder ssaBuilder;

    private final CFGBuilder cfgBuilder;

    private final DeadCodeDeletion deadCodeDeletion;

    public Optimizer(Module module) {
        this.module = module;
        this.ssaBuilder = new SSABuilder(module);
        this.cfgBuilder = new CFGBuilder(module);
        this.deadCodeDeletion = new DeadCodeDeletion(module);
    }

    public void run() {
        cfgBuilder.run();
        ssaBuilder.run();
        deadCodeDeletion.run();
    }
}
