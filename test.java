import java.io.*;
import java.nio.file.*;
import java.util.List;

public class TestRunner {

    private static final String MAS_JAR_PATH = "./Mars.jar";
    private static final String MIPS_CODE_PATH = "./mips.txt";
    private static final String CACHE_FILE = "./test_info/cache.txt";

    private static final String TESTFILE_PATH = "./testfile.txt";
    private static final String INPUT_PATH = "./input.txt";
    private static final String MODIFIED_TESTFILE_PATH = "./test_info/modified_testfile.c";
    private static final String MODIFIED_INPUT_PATH = "./test_info/modified_input.txt";
    private static final String MARS_OUT_PATH = "./test_info/mars_out.txt";
    private static final String GCC_EXE_PATH = "./test_info/main.out"; // Linux
    // private static final String GCC_EXE_PATH = ".\\script\\test_info\\main.exe"; // Windows
    private static final String GCC_OUT_PATH = "./test_info/gcc_out.txt";
    private static final String LOG_PATH = "./test_info/log.txt";

    private static class ColorfulPrint {
        static final int MODE_NORMAL = 1;
        static final int MODE_BOLD = 2;
        static final int COLOR_GRAY = 0;
        static final int COLOR_RED = 1;
        static final int COLOR_GREEN = 2;
        static final int COLOR_YELLOW = 3;
        static final int COLOR_BLUE = 4;
        static final int COLOR_PINK = 5;
        static final int COLOR_CYAN = 6;
        static final int COLOR_WHITE = 7;

        static void colorfulPrint(String msg, int mode, int color) {
            String head = "\033[";
            if (mode == MODE_NORMAL) {
                head = head + "0;";
            } else if (mode == MODE_BOLD) {
                head = head + "1;";
            } else {
                System.out.println("\033[1;31;40m" + " ***** MODE ERROR ***** " + "\033[0m");
                return;
            }
            if (0 <= color && color <= 7) {
                head = head + (30 + color) + ";40m";
            } else {
                System.out.println("\033[1;31;40m" + " ***** COLOR ERROR ***** " + "\033[0m");
            }
            String tail = "\033[0m";
            System.out.println(head + msg + tail);
        }
    }

    private static void makeJar() throws IOException, InterruptedException {
        ProcessBuilder javacProcess = new ProcessBuilder("javac", "-d", "./out", "-cp", "./src/", "-encoding", "UTF-8", "./src/Compiler.java");
        Process javac = javacProcess.start();
        javac.waitFor();

        ProcessBuilder jarProcess = new ProcessBuilder("jar", "cmvf", "../src/META-INF/MANIFEST.MF", "Compiler.jar", ".");
        File outDir = new File("./out");
        jarProcess.directory(outDir);
        Process jar = jarProcess.start();
        jar.waitFor();

        Files.copy(Paths.get("./out/Compiler.jar"), Paths.get("./Compiler.jar"), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void modifyInput() throws IOException {
        List<String> inputData = Files.readAllLines(Paths.get(INPUT_PATH));
        StringBuilder res = new StringBuilder();
        for (String line : inputData) {
            if (line.trim().isEmpty()) continue;
            res.append(String.join("\n", line.split(" "))).append("\n");
        }
        Files.write(Paths.get(MODIFIED_INPUT_PATH), res.toString().getBytes());
    }

    private static void modifyTestFile() throws IOException {
        List<String> oriTestFileContent = Files.readAllLines(Paths.get(TESTFILE_PATH));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MODIFIED_TESTFILE_PATH))) {
            String getintDef = """
                    #include <stdio.h>
                    #include <stdlib.h>
                    int getint() {
                        int input;
                        scanf("%d", &input);
                        return input;
                    }
                    """;
            writer.write(getintDef);
            for (String line : oriTestFileContent) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static void runWithoutStd() throws IOException, InterruptedException {
        modifyInput();
        modifyTestFile();
        ProcessBuilder compileProcess = new ProcessBuilder("java", "-jar", "Compiler.jar");
        compileProcess.redirectOutput(ProcessBuilder.Redirect.to(new File(CACHE_FILE)));
        Process compile = compileProcess.start();
        compile.waitFor();

        ProcessBuilder marsProcess = new ProcessBuilder("java", "-jar", MAS_JAR_PATH, "nc", MIPS_CODE_PATH);
        marsProcess.redirectInput(ProcessBuilder.Redirect.from(new File(MODIFIED_INPUT_PATH)));
        marsProcess.redirectOutput(ProcessBuilder.Redirect.to(new File(MARS_OUT_PATH)));
        Process mars = marsProcess.start();
        mars.waitFor();
    }

    private static boolean compareFilesStdOut(String outPath) throws IOException {
        List<String> marsOut = Files.readAllLines(Paths.get(MARS_OUT_PATH));
        List<String> stdOut = Files.readAllLines(Paths.get(outPath));

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_PATH))) {
            if (marsOut.size() != stdOut.size()) {
                logWriter.write("行数不一致！\n\n");
                return false;
            } else {
                for (int i = 0; i < marsOut.size(); i++) {
                    if (!marsOut.get(i).equals(stdOut.get(i))) {
                        logWriter.write(String.format("Error in line %d:\nExpected output is \"%s\", but your output is \"%s\"\n\n",
                                i + 1, stdOut.get(i), marsOut.get(i)));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void myTest() throws IOException, InterruptedException {
        System.out.println(">>> 开始对项目进行打包...");
        makeJar();
        String year = "2023";
        for (String type : new String[]{"A", "B", "C"}) {
            String path = String.format("./%s/%s", year, type);
            File[] files = new File(path).listFiles();
            if (files == null) continue;
            int testFileNum = files.length / 3;
            for (int i = 0; i < testFileNum; i++) {
                String testFileName = String.format("%s/testfile%d.txt", path, i + 1);
                String inputName = String.format("%s/input%d.txt", path, i + 1);
                String outputName = String.format("%s/output%d.txt", path, i + 1);

                Files.copy(Paths.get(testFileName), Paths.get(TESTFILE_PATH), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Paths.get(inputName), Paths.get(INPUT_PATH), StandardCopyOption.REPLACE_EXISTING);

                System.out.println(String.format(">>> 正在测试 %s....", testFileName));
                System.out.println(">>> 运行项目, 获得MIPS和标准答案的运行结果...");
                runWithoutStd();
                System.out.println(">>> 对运行结果进行比较...");
                boolean result = compareFilesStdOut(outputName);
                if (result) {
                    ColorfulPrint.colorfulPrint("输出正确！\n", ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN);
                } else {
                    ColorfulPrint.colorfulPrint("输出错误！\n", ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            myTest(); // 用于测试自己的代码可以改成java版本放到考试的时候用？用来存储2023辅助样例库
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
