import Config.CompilerHandler;
import Config.tasks;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException {
        tasks.isMIPSoutput = true;
        CompilerHandler.work();
    }
}
