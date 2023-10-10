package FrontEnd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class WordList {
    private final ArrayList<String> words;

    private final PrintStream printStream;

    public WordList(ArrayList<String> words, PrintStream ps) throws IOException {
        this.words = words;
        this.printStream = ps;
    }

    public void addWord(String word) {
        words.add(word);
    }

    public void write() {
        for (String word : words) {
            printStream.println(word);
        }
    }
}
