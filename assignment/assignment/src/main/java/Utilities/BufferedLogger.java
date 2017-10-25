package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;


@AllArgsConstructor
public class BufferedLogger {
    private String fileName;
    private final ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();

    public void log(String message) {
        this.buffer.add(message);
        if (buffer.size() % 1000 == 0) System.out.println(buffer.size());
    }

    @SneakyThrows(FileNotFoundException.class)
    public void persistLog() {
        @Cleanup
        PrintStream os = new PrintStream(new File(fileName));
        buffer.forEach(os::println);
    }
}
