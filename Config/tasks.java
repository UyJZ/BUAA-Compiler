package Config;

public class tasks {
    public static boolean isLexerAnalysis = false;
    public static boolean isParserAnalysis = false;
    public static boolean isErrorHandle = false;

    private static final String LexerOutputPath = "output.txt";

    private static final String ParserOutputPath = "output.txt";

    private static final String ErrorHandleOutputPath = "error.txt";

    public static String getInputPath() {
        return "testfile.txt";
    }

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

    public static String getOutputPath() {
        return isErrorHandle ? ErrorHandleOutputPath : LexerOutputPath;
    }
}
