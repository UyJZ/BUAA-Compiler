package Config;

import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import FrontEnd.Nodes.CompUnit;
import FrontEnd.Nodes.Node;
import FrontEnd.Parser.ParserController;
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
        } else if (tasks.isErrorHandle) {
            ParserController parserController = new ParserController(lexer.getTokenStream());
            parserController.parse().checkError();
            ErrorChecker.showErrorMsg(ps);
        } else if (tasks.isLLVMoutput) {
            ParserController parserController = new ParserController(lexer.getTokenStream());
            Node c = parserController.parse();
            c.genLLVMir();
            IRController.getInstance().Output(ps);
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
