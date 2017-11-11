package client;

import java.util.Map;

import utilities.OperationWrapper;

import org.kohsuke.args4j.Option;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class ResetClient {

    private String buildURL() {
        Map<String, String> endPoints = OperationWrapper.readConfig("./resource/end_points.yml");
        return "http://" + endPoints.get("server_ip") + ":" + endPoints.get("server_port") + "/assignment2/reset";
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
