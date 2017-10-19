package client;

import Utilities.BufferedLogger;
import Utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class LoadClient {
    private static Map<String, String> parse(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; ) {
            if (args[i].startsWith("-")) {
                map.put(args[i].substring(1), args[i + 1]);
                i += 2;
            } else {
                return map;
            }
        }
        return map;
    }


    public static void main(String[] args) {
        BlockingQueue<RFIDLiftData> blockingQueue = new LinkedBlockingDeque<>();
        List<LoadClientHandler> threadInstances;

        Map<String, String> params = parse(args);
        int threads = Integer.parseInt(params.getOrDefault("threads", "10"));
        String ip = params.getOrDefault("ip", "localhost");
        int port = Integer.parseInt(params.getOrDefault("port", "8080"));
        String sourcePath = params.get("source"); // "/Users/moyang/Documents/workspace/assignment1/resource/BSDSAssignment2Day1.ser"

        sourcePath = "/Users/moyang/Documents/workspace/assignment1/resource/BSDSAssignment2Day1.ser";

        BufferedLogger logger = new BufferedLogger();

        Stopwatch stopwatch = new Stopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads);
        ArrayList<RFIDLiftData> RFIDDataIn = null;


        try {
            RFIDDataIn = (ArrayList) (new ObjectInputStream(new FileInputStream(sourcePath))).readObject();
            System.out.println(RFIDDataIn.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        blockingQueue.addAll(RFIDDataIn);


        stopwatch.start();

        threadInstances = IntStream.range(0, threads).mapToObj(x -> LoadClientHandler.builder()
                .cb(cb)
                .logger(logger)
                .queue(blockingQueue)
                .ip(ip)
                .port(port)
                .build()
        ).collect(Collectors.toList());
        threadInstances.forEach(LoadClientHandler::start);

        try {
            cb.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }


        threadInstances.forEach(LoadClientHandler::interrupt);
        logger.log("WALL " + stopwatch.readAndReset());
        logger.persistLog();
    }
}
