package server;

import bsdsass2testdata.RFIDLiftData;
import utilities.BufferedLogger;
import utilities.Stopwatch;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("load/resortID/{resortID}/dayNum/{dayNum}/timestamp/{timestamp}/skierID/{skierID}/liftID/{liftID}")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class LoadService {
    private static final String LOGGER_NAME = "LoadServiceLogger";
    private static final String LOG_FILE_NAME = "LoadServiceLogger.txt";

    @POST
    public Response getMsg(@PathParam("resortID") int resortID,
                           @PathParam("dayNum") int dayNum,
                           @PathParam("timestamp") int timestamp,
                           @PathParam("skierID") int skierID,
                           @PathParam("liftID") int liftID
    ) {
        BufferedLogger logger = BufferedLogger.getOrCreateLogger(LOGGER_NAME, LOG_FILE_NAME);
        Stopwatch s = Stopwatch.createStopwatch();
        s.start();
        LoadBuffer.put(new RFIDLiftData(resortID, dayNum, skierID, liftID, timestamp));
        Response response = Response.ok("success").build();
        logger.log("POST_RESPONSE " + s.read() + " " + s.getStartTime());
        return response;
    }
}