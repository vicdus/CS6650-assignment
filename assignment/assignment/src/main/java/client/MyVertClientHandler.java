package client;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;

import Utilities.BufferedLogger;
import Utilities.OperationWrapper;
import Utilities.Stopwatch;
import lombok.Builder;

@Builder
public class MyVertClientHandler extends Thread {
    private final CyclicBarrier cb;
    private final BufferedLogger logger;
    private final ConcurrentLinkedQueue<Integer[]> queue;
    private final String ip;
    private int port;

    private String buildURL(Integer skierID, Integer dayNum) {
        return "http://" + ip + ":" + port + "/assignment2/myvert"
                + "/skierID/" + skierID.toString()
                + "/dayNum/" + dayNum.toString();
    }

    @Override
    public void run() {
        Response response;
        Client client = ClientBuilder.newClient()
                .property(ClientProperties.CONNECT_TIMEOUT, Integer.MAX_VALUE)
                .property(ClientProperties.READ_TIMEOUT, Integer.MAX_VALUE);
        Stopwatch s = Stopwatch.createStopwatch();

        Integer[] params = queue.poll();
        while (params != null) {
            s.start();
            response = client
                    .target(buildURL(params[0], params[1]))
                    .request(MediaType.TEXT_PLAIN)
                    .get();
            response.close();
            logger.log("GET " + s.read() + " " + s.getStartTime());
            params = queue.poll();
        }
        OperationWrapper.uninterruptibleCyclicBarrierAwait(cb);
    }
}
