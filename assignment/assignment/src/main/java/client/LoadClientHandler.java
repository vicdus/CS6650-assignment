package client;

import Utilities.BufferedLogger;
import Utilities.UninterruptibleCyclicBarrier;
import bsdsass2testdata.RFIDLiftData;
import lombok.Builder;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

@Builder
public final class LoadClientHandler extends Thread {
    private final CyclicBarrier cb;
    private final BufferedLogger logger;
    private final ConcurrentLinkedQueue<RFIDLiftData> queue;
    private final String ip;
    private int port;

    private String buildURL(RFIDLiftData data) {
        return "http://" + ip + ":" + port + "/assignment2/"
                + "load/resortID/" + data.getResortID()
                + "/dayNum/" + data.getDayNum()
                + "/timestamp/" + data.getTime()
                + "/skierID/" + data.getSkierID()
                + "/liftID/" + data.getLiftID();
    }


    @Override
    public void run() {
        Response response;
        Client client = ClientBuilder.newClient()
                .property(ClientProperties.CONNECT_TIMEOUT, Integer.MAX_VALUE)
                .property(ClientProperties.READ_TIMEOUT, Integer.MAX_VALUE);

        RFIDLiftData r = queue.poll();
        while (r != null) {
            response = client
                    .target(buildURL(r))
                    .request(MediaType.TEXT_PLAIN)
                    .post(Entity.text(null));
            response.close();
            r = queue.poll();
        }
        UninterruptibleCyclicBarrier.await(cb);
    }
}
