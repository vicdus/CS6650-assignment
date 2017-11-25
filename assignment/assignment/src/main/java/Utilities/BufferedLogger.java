package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.Uninterruptibles;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferedLogger {
    private static final Map<String, BufferedLogger> loggers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();
    @NonNull
    private String fileName;

    static {
        Thread t = new Thread(() -> {
            while (true) {
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                for (BufferedLogger logger : loggers.values()) {
                    logger.persist();
                }
            }
        });
        t.start();
    }

    public synchronized static BufferedLogger getOrCreateLogger(String loggerName, String fileName) {
        if (loggers.containsKey(loggerName)) {
            return loggers.get(loggerName);
        } else {
            BufferedLogger logger = new BufferedLogger(fileName);
            loggers.put(loggerName, logger);
            return logger;
        }
    }


    public void log(String message) {
        this.buffer.add(message);
    }

    public void persist() {
        if (buffer.isEmpty()) return;
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            String log = buffer.poll();
            while (log != null) {
                fileWriter.write(log + "\n");
                log = buffer.poll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
