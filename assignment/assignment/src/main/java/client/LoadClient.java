package client;

import Utilities.BufferedLogger;
import Utilities.OperationWrapper;
import Utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;

import org.kohsuke.args4j.Option;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class LoadClient {

    @Option(name = "-ip", usage = "ip address, default: localhost")
    private String ip = "localhost";

    @Option(name = "-threads", usage = "threads number, default: 10")
    private int threads = 10;

    @Option(name = "-port", usage = "port number, default: 8080")
    private int port = 8080;

    @Option(name = "-source", usage = "ser file path")
    private String sourcePath = "./resource/BSDSAssignment2Day1.ser";

    @Option(name = "-logfile", usage = "path of logging file, default: log.txt")
    private String logFile = "log.txt";

    private void start(String[] args) {
        OperationWrapper.parseSilently(args, this, "Invalid arguments!");

        ConcurrentLinkedQueue<RFIDLiftData> queue;
        List<LoadClientHandler> threadInstances;

        BufferedLogger logger = new BufferedLogger(logFile);
        Stopwatch stopwatch = Stopwatch.createStopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads + 1);

        queue = new ConcurrentLinkedQueue<>(OperationWrapper.loadData(sourcePath));
        threadInstances = IntStream.range(0, threads).mapToObj(x -> LoadClientHandler.builder()
                .cb(cb)
                .logger(logger)
                .queue(queue)
                .ip(ip)
                .port(port)
                .build()
        ).collect(Collectors.toList());

        stopwatch.start();
        threadInstances.forEach(LoadClientHandler::start);

        OperationWrapper.uninterruptibleCyclicBarrierAwait(cb);

        logger.log("WALL " + stopwatch.readAndReset());
        logger.persistLog();
    }


    public static void main(String[] args) {
        new LoadClient().start(args);
    }
}
