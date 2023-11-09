import Config.CompilerHandler;
import Config.tasks;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException {
        tasks.setLLVMOutputForHw4();
        CompilerHandler.work();
    }
}
