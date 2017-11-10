package client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import utilities.BufferedLogger;
import utilities.OperationWrapper;
import utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;

import org.kohsuke.args4j.Option;

public class MyVertClient {

    private static final String LOGGER_NAME = "MyVertClientLogger";
    private static final String LOG_FILE = "MyVertClientLog.txt";

    @Option(name = "-threads", usage = "threads number, default: 100")
    private int threads = 100;

    @Option(name = "-source", usage = "ser file path")
    private String sourcePath = "./resource/BSDSAssignment2Day1.ser";


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new MyVertClient().start(args);
    }


    private void start(String[] args) throws IOException, ClassNotFoundException {
        OperationWrapper.parseSilently(args, this, "Invalid arguments!");

        ConcurrentLinkedQueue<Integer[]> queue;
        List<MyVertClientHandler> threadInstances;

        BufferedLogger logger = BufferedLogger.getOrCreateLogger(LOGGER_NAME, LOG_FILE);
        Stopwatch stopwatch = Stopwatch.createStopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads + 1);

        queue = new ConcurrentLinkedQueue<>(OperationWrapper
                .loadData(sourcePath)
                .stream()
                .map(RFIDLiftData::getSkierID)
                .distinct()
                .map(skierID -> new Integer[]{skierID, 1})
                .collect(Collectors.toList())
        );

        threadInstances = IntStream.range(0, threads).mapToObj(x -> MyVertClientHandler.builder()
                .cb(cb)
                .logger(logger)
                .queue(queue)
                .ip(OperationWrapper.readConfig("./resource/end_points.yml").get("server_ip"))
                .port(Integer.parseInt(OperationWrapper.readConfig("./resource/end_points.yml").get("server_port")))
                .build()
        ).collect(Collectors.toList());

        stopwatch.start();
        threadInstances.forEach(MyVertClientHandler::start);
        OperationWrapper.uninterruptibleCyclicBarrierAwait(cb);
        logger.log("WALL " + stopwatch.readAndReset());
        logger.persist();
    }
}
