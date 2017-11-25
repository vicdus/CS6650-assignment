package client;

import utilities.BufferedLogger;
import utilities.OperationWrapper;
import utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;

import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class LoadClient {

    private static final String LOGGER_NAME = "LoadClientLogger";
    private static final String LOG_FILE = "LoadClientLogger.txt";

    @Option(name = "-threads", usage = "threads number, default: 100")
    private int threads = 100;

    @Option(name = "-source", usage = "ser file path")
    private String sourcePath = "./resource/BSDSAssignment2Day1.ser";

    private void start(String[] args) throws IOException, ClassNotFoundException {
        OperationWrapper.parseSilently(args, this, "Invalid arguments!");

        ConcurrentLinkedQueue<RFIDLiftData> queue;
        List<LoadClientHandler> threadInstances;

        BufferedLogger logger = BufferedLogger.getOrCreateLogger(LOGGER_NAME, LOG_FILE);
        Stopwatch stopwatch = Stopwatch.createStopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads + 1);

        queue = new ConcurrentLinkedQueue<>(OperationWrapper.loadData(sourcePath));
        threadInstances = IntStream.range(0, threads).mapToObj(x -> LoadClientHandler.builder()
                .cb(cb)
                .logger(logger)
                .queue(queue)
                .ip(OperationWrapper.readConfig("./resource/end_points.yml").get("server_ip"))
                .port(Integer.parseInt(OperationWrapper.readConfig("./resource/end_points.yml").get("server_port")))
                .build()
        ).collect(Collectors.toList());

        stopwatch.start();
        threadInstances.forEach(LoadClientHandler::start);

        OperationWrapper.uninterruptibleCyclicBarrierAwait(cb);

        logger.log("WALL " + stopwatch.readAndReset());
        logger.persist();
        System.exit(0);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new LoadClient().start(args);
    }
}
