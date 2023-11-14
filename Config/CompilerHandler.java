package Config;

import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import FrontEnd.Nodes.CompUnit;
import FrontEnd.Nodes.Node;
import FrontEnd.Parser.ParserController;
import FrontEnd.Symbol.SymbolManager;
import llvm_ir.IRController;

import javax.swing.text.html.parser.Parser;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class CompilerHandler {

    public static void work() throws IOException {
        StringBuilder stringBuilder = readFile(tasks.getInputPath());
        Lexer lexer = new Lexer(stringBuilder);
        lexer.analysisLexer();
        FileOutputStream fs = new FileOutputStream(tasks.getOutputPath());
        PrintStream ps = new PrintStream(fs);
        if (tasks.isLexerAnalysis) {
            ArrayList<Token> tokens = lexer.getTokens();
            for (Token token : tokens) {
                ps.println(token);
            }
        } else if (tasks.isParserAnalysis) {
            ParserController parserController = new ParserController(lexer.getTokenStream());
            parserController.parse().print(ps);
        } else if (tasks.isErrorHandle && !tasks.isLLVMoutput) {
            ParserController parserController = new ParserController(lexer.getTokenStream());
            parserController.parse().checkError();
            ErrorChecker.showErrorMsg(ps);
        } else if (tasks.isLLVMoutput) {
            assert (tasks.getOutputPath().equals("llvm_ir.txt"));
            ParserController parserController = new ParserController(lexer.getTokenStream());
            Node c = parserController.parse();
            c.checkError();
            ErrorChecker.showErrorMsg(new PrintStream(new FileOutputStream("error.txt")));
            if (ErrorChecker.getErrors().size() == 0) {
                SymbolManager.getInstance().flush();
                c.genLLVMir();
                IRController.getInstance().Output(ps);
            }
        }
    }


    private static StringBuilder readFile(String filePath) throws FileNotFoundException {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line.strip());
                stringBuffer.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

}
