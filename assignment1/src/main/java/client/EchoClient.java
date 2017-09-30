package client;

import Utilities.BufferedLogger;
import Utilities.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class EchoClient {
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
        Map<String, String> params = parse(args);
        int threads = params.containsKey("threads") ? Integer.parseInt(params.get("threads")) : 10;
        int iterations = params.containsKey("iterations") ? Integer.parseInt(params.get("iterations")) : 100;
        String ip = params.get("ip");
        int port = Integer.parseInt(params.get("port"));

        BufferedLogger logger = new BufferedLogger();

        Stopwatch stopwatch = new Stopwatch();
        CyclicBarrier cb = new CyclicBarrier(threads, () -> {
            logger.log("WALL " + stopwatch.readAndReset());
            logger.persistLog();
        });

        String param = "some_string";
        stopwatch.start();
        for (int i = 0; i < threads; i++) {
            (new EchoClientHandler(iterations, ip, port, param, cb, logger)).start();
        }
    }
}
