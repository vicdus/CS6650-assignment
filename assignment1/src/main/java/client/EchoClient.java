package client;

import Utilities.Timer;

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

        Timer timer = new Timer();
        CyclicBarrier cb = new CyclicBarrier(threads, () -> System.out.println("WALL " + timer.readAndReset()));
        timer.start();
        for (int i = 0; i < threads; i++) {
            (new EchoClientHandler(iterations, ip, port, cb)).start();
        }
    }
}
