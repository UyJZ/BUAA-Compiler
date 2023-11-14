package Config;

public class tasks {
    public static boolean isLexerAnalysis = false;
    public static boolean isParserAnalysis = false;
    public static boolean isErrorHandle = false;

    public static boolean isLLVMoutput = false;

    private static String LexerOutputPath = "output.txt";

    private static String ParserOutputPath = "output.txt";

    public static String ErrorHandleOutputPath = "error.txt";

    public static String getInputPath() {
        return "testfile.txt";
    }

    public static String LLVMOutputPath = "llvm_ir.txt";

    public static void setLexerAnalysisForHw1() {
        tasks.isLexerAnalysis = true;
        tasks.isErrorHandle = false;
        tasks.isParserAnalysis = false;
    }

    public static void setParserAnalysisForHw2() {
        tasks.isLexerAnalysis = false;
        tasks.isErrorHandle = false;
        tasks.isParserAnalysis = true;
    }

    public static void setErrorHandleForHw3() {
        tasks.isLexerAnalysis = false;
        tasks.isErrorHandle = true;
        tasks.isParserAnalysis = false;
    }

    public static void setLLVMOutputForHw4() {
        tasks.isLexerAnalysis = false;
        tasks.isErrorHandle = true;
        tasks.isParserAnalysis = false;
        tasks.isLLVMoutput = true;
    }

    public static String getOutputPath() {
        return isLLVMoutput ? LLVMOutputPath : isErrorHandle ? ErrorHandleOutputPath : isLexerAnalysis ? LexerOutputPath : ParserOutputPath;
    }
}
