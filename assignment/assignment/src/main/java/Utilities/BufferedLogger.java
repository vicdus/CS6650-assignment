package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
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
            PrintStream os = new PrintStream(new File(fileName));
            buffer.forEach(os::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
