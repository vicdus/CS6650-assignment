package client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Utilities.BufferedLogger;
import Utilities.OperationWrapper;
import Utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;

import org.glassfish.jersey.client.ClientProperties;
import org.kohsuke.args4j.Option;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyVertClient {
    @Option(name = "-ip", usage = "ip address, default: localhost")
    private String ip = "localhost";

    @Option(name = "-port", usage = "port number, defalut: 8080")
    private int port = 8080;

    @Option(name = "-logfile", usage = "path of logging file, default: log.txt")
    private String logFile = "log.txt";

    @Option(name = "-threads", usage = "threads number, default: 100")
    private int threads = 100;

    @Option(name = "-source", usage = "ser file path")
    private String sourcePath = "./resource/BSDSAssignment2Day1.ser";


    public static void main(String[] args) {
        new MyVertClient().start(args);
    }


    private void start(String[] args) {
        OperationWrapper.parseSilently(args, this, "Invalid arguments!");


        ConcurrentLinkedQueue<Integer[]> queue;
        List<MyVertClientHandler> threadInstances;

        BufferedLogger logger = new BufferedLogger(logFile);
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
                .ip(ip)
                .port(port)
                .build()
        ).collect(Collectors.toList());

        stopwatch.start();
        threadInstances.forEach(MyVertClientHandler::start);
        OperationWrapper.uninterruptibleCyclicBarrierAwait(cb);
        logger.log("WALL " + stopwatch.readAndReset());
        logger.persistLog();
    }
}
