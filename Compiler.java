import FrontEnd.lexer.Lexer;
import FrontEnd.lexer.Token;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = readFile("./testfile.txt");
        Lexer lexer = new Lexer(stringBuilder);
        ArrayList<Token> tokens = lexer.analysisLexer();
        FileWriter fileWriter = new FileWriter("./output.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (Token token : tokens) {
            printWriter.println(token);
        }
        printWriter.close();
        fileWriter.close();
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
