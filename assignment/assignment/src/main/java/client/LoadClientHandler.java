package client;

import Utilities.BufferedLogger;
import bsdsass2testdata.RFIDLiftData;
import lombok.Builder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Builder
public final class LoadClientHandler extends Thread {
    private final CyclicBarrier cb;
    private final BufferedLogger logger;
    private final BlockingQueue<RFIDLiftData> queue;
    private final String ip;
    private int port;

    private String buildURL(RFIDLiftData data) {


        String res = "http://" + ip + ":" + port + "/assignment2/" // TODO
                + "load/resortID/" + data.getResortID()
                + "/dayNum/" + data.getDayNum()
                + "/timestamp/" + data.getTime()
                + "/skierID/" + data.getSkierID()
                + "/liftID/" + data.getLiftID();

        System.out.println(res);
        return res;
    }


    @Override
    public void run() {
        Response response = null;

        while (!queue.isEmpty()) {
            try {
                RFIDLiftData r = queue.take();
                System.out.println(r);
                response = ClientBuilder.newClient()
                        .target(buildURL(r))
                        .request(MediaType.TEXT_PLAIN)
                        .post(Entity.text(null));
                if (response.getStatus() != 200) throw new Exception();
            } catch (InterruptedException e) {
                System.out.println("Done!");
            } catch (Exception e) {
                System.out.println("ERROR STATUS CODE");
            } finally {
                if (response != null) response.close();
            }
        }

        try {
            cb.await();
        } catch (InterruptedException e) {
            System.out.println("Done!");
        } catch (BrokenBarrierException e) {
            System.out.println("BrokenBarrierException");
        } finally {
            if (response != null) response.close();
        }
    }
}
