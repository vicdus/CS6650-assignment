package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.ho.yaml.Yaml;
import org.kohsuke.args4j.CmdLineParser;

import com.google.common.collect.*;

import bsdsass2testdata.RFIDLiftData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationWrapper {

    @SneakyThrows
    public static void parseSilently(String[] args, Object bean, String errmsg) {
        new CmdLineParser(bean).parseArgument(args);
    }

    @SneakyThrows
    public synchronized static Map<String, String> readConfig(String ymlFilePath) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        ((Map<Object, Object>) Yaml.load(new File(ymlFilePath))).forEach((k, v) -> builder.put(k.toString(), v.toString()));
        return builder.build();
    }

    public static void uninterruptibleCyclicBarrierAwait(CyclicBarrier cb) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    cb.await();
                    return;
                } catch (InterruptedException | BrokenBarrierException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized static List<RFIDLiftData> loadData(String src) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(src)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (ArrayList) (ois).readObject();
            }
        }
    }
}
