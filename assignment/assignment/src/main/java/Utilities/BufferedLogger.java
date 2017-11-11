package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferedLogger {
    private static final Map<String, BufferedLogger> loggers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();
    private static final int BUFFER_SIZE = 1000;


    static {
        Thread t = new Thread(() -> {
            while (true) {
                for (BufferedLogger logger : loggers.values()) {
                    if (logger.buffer.size() > BUFFER_SIZE) logger.persist();
                }
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    @NonNull
    private String fileName;

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
