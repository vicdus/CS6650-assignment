package client;

import Utilities.BufferedLogger;
import Utilities.Stopwatch;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class EchoClientHandler extends Thread {
    private final int iteration;
    private final CyclicBarrier cb;
    private final String URL;
    private final BufferedLogger logger;

    public EchoClientHandler(int iteration, String ip_address, int port, String param, CyclicBarrier cb, BufferedLogger logger) {
        this.iteration = iteration;
        this.cb = cb;
        this.URL = "http://" + ip_address + ":" + port + "/assignment1/" + param;
        this.logger = logger;
    }

    @Override
    public void run() {
        Stopwatch stopwatch = new Stopwatch();
        Response response = null;
        for (int i = 0; i < iteration; i++) {
            long start = System.currentTimeMillis();

            stopwatch.start();
            try {
                response = ClientBuilder.newClient().target(URL).request(MediaType.TEXT_PLAIN).post(Entity.text(null));
                if (response.getStatus() != 200) throw new Exception("failed for any reason");
            } catch (Exception e) {
                logger.log("POST FAILURE");
            } finally {
                if (response != null) response.close();
            }
            logger.log("POST " + stopwatch.readAndReset() + " " + start);

            start = System.currentTimeMillis();
            stopwatch.readAndReset();
            try {
                response = ClientBuilder.newClient().target(URL).request(MediaType.TEXT_PLAIN).get();
                if (response.getStatus() != 200) throw new Exception("failed for any reason");
            } catch (Exception e) {
                logger.log("GET FAILURE");
            } finally {
                if (response != null) response.close();
            }
            logger.log("GET " + stopwatch.readAndReset() + " " + start);
        }

        try {
            cb.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            logger.log("CB FAILURE");
        }
    }
}
