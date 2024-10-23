package Config;

import BackEnd.MIPS.MipsBuilder;
import FrontEnd.ErrorProcesser.ErrorList;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import FrontEnd.AbsSynTreeNodes.Node;
import FrontEnd.Parser.Parser;
import FrontEnd.SymbolTable.SymbolTableBuilder;
import MidEnd.Optimizer;
import Ir_LLVM.LLVM_Builder;

import java.io.*;
import java.util.ArrayList;

public class CompilerDriver {

    public static void work() throws IOException {
        StringBuilder stringBuilder = readFile(Tasks.getInputPath());
        Lexer lexer = new Lexer(stringBuilder);
        lexer.analysisLexer();
        FileOutputStream fs = new FileOutputStream(Tasks.getOutputPath());
        PrintStream ps = new PrintStream(fs);
        if (Tasks.isLexerAnalysis) {
            ArrayList<Token> tokens = lexer.getTokens();
            for (Token token : tokens) {
                ps.println(token);
            }
        } else if (Tasks.isParserAnalysis) {
            Parser parser = new Parser(lexer.getTokenStream());
            parser.parse().print(ps);
        } else if (Tasks.isErrorHandle && !Tasks.isLLVMoutput && !Tasks.isMIPSoutput) {
            Parser parser = new Parser(lexer.getTokenStream());
            parser.parse().checkError();
            ErrorList.showErrorMsg(ps);
        } else if (Tasks.isLLVMoutput) {
            assert (Tasks.getOutputPath().equals("llvm_ir.txt"));
            Parser parser = new Parser(lexer.getTokenStream());
            Node c = parser.parse();
            c.checkError();
            ErrorList.showErrorMsg(new PrintStream(new FileOutputStream("error.txt")));
            if (ErrorList.getErrors().size() == 0) {
                SymbolTableBuilder.getInstance().flush();
                c.genLLVMir();
                if (Tasks.isOptimize) {
                    Optimizer optimizer = new Optimizer(LLVM_Builder.getInstance().getModule());
                    optimizer.run();
                } else {
                    LLVM_Builder.getInstance().getModule().setName();
                }
                LLVM_Builder.getInstance().Output(ps);
            }
        } else if (Tasks.isMIPSoutput) {
            Parser parser = new Parser(lexer.getTokenStream());
            Node c = parser.parse();
            if (Tasks.isErrorHandle) {
                c.checkError();
                ErrorList.showErrorMsg(new PrintStream(new FileOutputStream("error.txt")));
            }
            if (ErrorList.getErrors().size() == 0) {
                SymbolTableBuilder.getInstance().flush();
                c.genLLVMir();
                if (Tasks.isOptimize) {
                    Optimizer optimizer = new Optimizer(LLVM_Builder.getInstance().getModule());
                    optimizer.run();
                }else {
                    LLVM_Builder.getInstance().getModule().setName();
                }
                PrintStream ps1 = new PrintStream(new FileOutputStream("llvm_ir.txt"));
                PrintStream ps2 = new PrintStream(new FileOutputStream("mips.txt"));
                LLVM_Builder.getInstance().Output(ps1);
                MipsBuilder.getInstance().setModule(LLVM_Builder.getInstance().getModule());
                MipsBuilder.getInstance().run();
                MipsBuilder.getInstance().print(ps2);
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
