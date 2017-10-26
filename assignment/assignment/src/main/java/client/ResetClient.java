package client;

import utilities.OperationWrapper;

import org.kohsuke.args4j.Option;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class ResetClient {

    @Option(name = "-ip", usage = "ip address, default: localhost")
    private String ip = "localhost";

    @Option(name = "-port", usage = "port number, defalut: 8080")
    private int port = 8080;

    private String buildURL() {
        return "http://" + ip + ":" + port + "/assignment2/reset";
    }

    private void reset(String[] args) {
        OperationWrapper.parseSilently(args, this, "Invalid arguments!");

        ClientBuilder
                .newClient()
                .target(buildURL())
                .request(MediaType.TEXT_PLAIN).delete();
    }

    public static void main(String[] args) {
        new ResetClient().reset(args);
    }
}
