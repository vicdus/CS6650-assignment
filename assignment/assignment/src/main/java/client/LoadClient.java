package client;

import Utilities.BufferedLogger;
import Utilities.Stopwatch;
import Utilities.UninterruptibleCyclicBarrier;
import bsdsass2testdata.RFIDLiftData;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class LoadClient {

    @Option(name = "-ip", usage = "ip address, default: localhost")
    private String ip = "localhost";

    @Option(name = "-threads", usage = "threads number, defalut: 10")
    private int threads = 10;

    @Option(name = "-port", usage = "port number, defalut: 8080")
    private int port = 8080;

    @Option(name = "-source", usage = "ser file path")
    private String sourcePath = "./resource/BSDSAssignment2Day1.ser";


    private void start(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Invalid arguments!");
            System.exit(1);
        }

        ConcurrentLinkedQueue<RFIDLiftData> queue;
        List<LoadClientHandler> threadInstances;

        BufferedLogger logger = new BufferedLogger();
        Stopwatch stopwatch = new Stopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads + 1);
        ArrayList RFIDDataIn = null;

        try {
            RFIDDataIn = (ArrayList) (new ObjectInputStream(new FileInputStream(sourcePath))).readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Loading data failed.");
            System.exit(1);
        }

        queue = new ConcurrentLinkedQueue<>(RFIDDataIn);

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

        UninterruptibleCyclicBarrier.await(cb);

        logger.log("WALL " + stopwatch.readAndReset());
        logger.persistLog();
    }


    public static void main(String[] args) {
        new LoadClient().start(args);
    }
}
