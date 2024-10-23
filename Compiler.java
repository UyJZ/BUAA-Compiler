import Config.CompilerDriver;
import Config.Tasks;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException, RuntimeException {
        /*
        -l : Lexer analysis
        -p : Parser analysis
        -e : Error handle
        -i : llvm ir output
        -m : mips output
        -io : llvm ir and optimize
        -mo : mips and optimize
        -ie : llvm ir and error handle
        -me : mips and error handle
        -ioe : llvm ir and optimize and error handle
        -moe : mips and optimize and error handle
         */
        Tasks.isMIPSoutput = true;
        Tasks.isOptimize = false;
        Tasks.isLLVMoutput = false;
        Tasks.isErrorHandle = true;
        CompilerDriver.work();
        //throw new RuntimeException("Compiler finished");
    }
}
