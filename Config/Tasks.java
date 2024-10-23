package Config;

public class Tasks {
    public static boolean isLexerAnalysis = false;
    public static boolean isParserAnalysis = false;
    public static boolean isErrorHandle = false;

    public static boolean isMIPSoutput = true;

    public static boolean isSetNameAfterGen = true;

    public static boolean isOptimize = true;

    public static boolean isLLVMoutput = false;

    private static String LexerOutputPath = "output.txt";

    private static String ParserOutputPath = "output.txt";

    public static String ErrorHandleOutputPath = "error.txt";

    public static String MIPSOutputPath = "mips.txt";

    public static String getInputPath() {
        return "testfile.txt";
    }

    public static String LLVMOutputPath = "llvm_ir.txt";

    public static void setLexerAnalysisForHw1() {
        Tasks.isLexerAnalysis = true;
        Tasks.isErrorHandle = false;
        Tasks.isParserAnalysis = false;
    }

    public static void setParserAnalysisForHw2() {
        Tasks.isLexerAnalysis = false;
        Tasks.isErrorHandle = false;
        Tasks.isParserAnalysis = true;
    }

    public static void setErrorHandleForHw3() {
        Tasks.isLexerAnalysis = false;
        Tasks.isErrorHandle = true;
        Tasks.isParserAnalysis = false;
    }

    public static void setLLVMOutputForHw4() {
        Tasks.isLexerAnalysis = false;
        Tasks.isErrorHandle = true;
        Tasks.isParserAnalysis = false;
        Tasks.isLLVMoutput = true;
    }

    public static String getOutputPath() {
        return isLLVMoutput ? LLVMOutputPath : isErrorHandle ? ErrorHandleOutputPath : isLexerAnalysis ? LexerOutputPath : ParserOutputPath;
    }

    public static boolean isIsSetNameAfterGen() {
        return isSetNameAfterGen;
    }
}
