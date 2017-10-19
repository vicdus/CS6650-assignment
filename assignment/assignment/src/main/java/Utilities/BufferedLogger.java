package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferedLogger {
    private static final String fileName = "log.txt";
    private ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();

    public void log(String message) {
        this.buffer.add(message);
    }

    public void persistLog() {
        try {
            System.setOut(new PrintStream(new File(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String s : buffer) {
            System.out.println(s);
        }
    }
}
