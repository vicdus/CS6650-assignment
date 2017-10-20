package client;

import bsdsass2testdata.RFIDLiftData;
import org.glassfish.jersey.client.ClientProperties;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyVertClient {
    @Option(name = "-ip", usage = "ip address, default: localhost")
    private String ip = "localhost";

    @Option(name = "-port", usage = "port number, defalut: 8080")
    private int port = 8080;

    @Option(name = "-skierID", usage = "ID of Skier", required = true)
    private int skierID;

    @Option(name = "-dayNum", usage = "Day", required = true)
    private int dayNum;

    public static void main(String[] args) {
        new MyVertClient().start(args);
    }

    private String buildURL() {
        return "http://" + ip + ":" + port + "/assignment2/myvert"
                + "/skierID/" + skierID
                + "/dayNum/" + dayNum;
    }

    private void start(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Invalid arguments!");
            System.exit(1);
        }

        Response r = ClientBuilder.newClient()
                .property(ClientProperties.CONNECT_TIMEOUT, Integer.MAX_VALUE)
                .property(ClientProperties.READ_TIMEOUT, Integer.MAX_VALUE)
                .target(buildURL())
                .request(MediaType.TEXT_PLAIN)
                .get();

        System.out.println(r.readEntity(Integer.class));
    }

}
