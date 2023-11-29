package MidEnd;

import llvm_ir.Module;

public class Optimizer {
    private final Module module;

    private final SSABuilder ssaBuilder;

    private final CFGBuilder cfgBuilder;

    public Optimizer(Module module) {
        this.module = module;
        this.ssaBuilder = new SSABuilder(module);
        this.cfgBuilder = new CFGBuilder(module);
    }

    public void run() {
        cfgBuilder.run();
        ssaBuilder.run();
    }
}
