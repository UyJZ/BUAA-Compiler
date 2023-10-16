import FrontEnd.ErrorManager.ErrorChecker;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import FrontEnd.Nodes.Node;
import FrontEnd.Parser.ParserController;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = readFile("./testfile.txt");
        Lexer lexer = new Lexer(stringBuilder);
        lexer.analysisLexer();
        //ArrayList<Token> tokens = lexer.analysisLexer();
        //hw2 LexerAnalysis:
        //LexerAnalysis(stringBuilder, tokens);
        //hw3 ParserAnalysis:
        FileOutputStream fos = new FileOutputStream("error.txt");
        PrintStream ps = new PrintStream(fos);
        ParserController parserController = new ParserController(lexer.getTokenStream());
        Node c = parserController.parse();
        c.checkError();
        ErrorChecker.showErrorMsg(ps);
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

    public static void LexerAnalysis(StringBuilder stringBuilder, ArrayList<Token> tokens) throws IOException {
        FileWriter fileWriter = new FileWriter("./output.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (Token token : tokens) {
            printWriter.println(token);
        }
        printWriter.close();
        fileWriter.close();
    }

    public static void ParserAnalysis(StringBuilder stringBuilder, ArrayList<Token> tokens) throws IOException {
        FileWriter fileWriter = new FileWriter("./output.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (Token token : tokens) {
            printWriter.println(token);
        }
        printWriter.close();
        fileWriter.close();
    }
}
