package client;

import Utilities.Timer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class EchoClientHandler extends Thread {
    private final int iteration;
    private final CyclicBarrier cb;
    private final String URL;

    public EchoClientHandler(int iteration, String ip_address, int port, CyclicBarrier cb) {
        this.iteration = iteration;
        this.cb = cb;
        this.URL = "http://" + ip_address + ":" + port + "/assignment1/";
    }

    @Override
    public void run() {
        // redirect output to file
        try {
            System.setOut(new PrintStream(new File("log.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Timer t = new Timer();
        for (int i = 0; i < iteration; i++) {
            long start = System.currentTimeMillis();
            try {
                t.start();
                ClientBuilder.newClient().target(URL).request(MediaType.TEXT_PLAIN).post(Entity.text(null)).close();
            } catch (Exception e) {
                System.out.println("POST FAILURE");
            }
            System.out.println("POST " + t.readAndReset() + " " + start);

            start = System.currentTimeMillis();
            try {
                t.readAndReset();
                ClientBuilder.newClient().target(URL).request(MediaType.TEXT_PLAIN).get().close();
            } catch (Exception e) {
                System.out.println("GET FAILURE");
            }
            System.out.println("GET " + t.readAndReset() + " " + start);
        }

        try {
            cb.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            System.out.println("CB FAILURE");
        }
    }
}
