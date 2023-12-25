package MyPackage;

import MyPackage.IR.IRModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class OutPut {
    static File outPutFile = new File("output.txt");
    static FileWriter outPut;
    static File errorFile = new File("error.txt");
    static File llvmFile = new File("llvm_ir.txt");
    static File mipsFile = new File("mips.txt");
    static FileWriter errorOut;
    static FileWriter llvmOut;
    static FileWriter mipsOut;
    static ArrayList<item> items = new ArrayList<>();
    static class item {
        public char type;
        public int line;

        public item(char type, int line) {
            this.type = type;
            this.line = line;
        }

        public int getLine() {
            return line;
        }
    }

    static {
        try {
            outPut = new FileWriter(outPutFile);
            errorOut = new FileWriter(errorFile);
            llvmOut = new FileWriter(llvmFile);
            mipsOut = new FileWriter(mipsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void print(String string) {
        try {
            outPut.write(string + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void printError(char type, int line) {
        items.add(new item(type, line));
        IRModule.setError(true);
    }

    static public void printLlvm(String string) {
        try {
            llvmOut.write(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void printMips(String string) {
        try {
            mipsOut.write(string + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void printMips2(String string) {
        try {
            mipsOut.write(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void close() {
        items.sort(Comparator.comparing(item::getLine));
        for (int i = 0; i < items.size(); i++) {
            try {
                errorOut.write(items.get(i).line + " " + items.get(i).type + '\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            outPut.close();
            errorOut.close();
            llvmOut.close();
            mipsOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
