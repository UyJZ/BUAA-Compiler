import Config.CompilerHandler;
import Config.tasks;
import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import FrontEnd.Nodes.Node;
import FrontEnd.Parser.ParserController;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        tasks.setLLVMOutputForHw4();
        CompilerHandler.work();
    }
}
