package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class BufferedLogger {
    private String fileName;
    private final ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();

    public void log(String message) {
        this.buffer.add(message);
    }

    public void persistLog() throws FileNotFoundException {
        try (PrintStream ps = new PrintStream(new File(fileName))) {
            buffer.forEach(ps::println);
        }
    }
}
