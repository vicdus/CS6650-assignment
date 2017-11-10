package server;

import utilities.BufferedLogger;
import utilities.DBConnectionPoolWrapper;
import utilities.RFIDLiftDataDAO;
import utilities.Stopwatch;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

@Path("myvert/skierID/{skierID}/dayNum/{dayNum}")
@Produces(MediaType.TEXT_PLAIN)
public class MyVertService {
    private static final String LOGGER_NAME = "LoadServiceLogger";
    private static final String LOG_FILE_NAME = "MyVertServiceLogger.txt";

    @GET
    public Response get(@PathParam("skierID") int skierID, @PathParam("dayNum") int dayNum) throws SQLException {
        BufferedLogger logger = BufferedLogger.getOrCreateLogger(LOGGER_NAME, LOG_FILE_NAME);
        Stopwatch s = Stopwatch.createStopwatch();
        s.start();
        Connection conn = DBConnectionPoolWrapper.getConnection();
        Integer[] res = new RFIDLiftDataDAO(conn).vert(skierID, dayNum);
        DbUtils.closeQuietly(conn);
        Response response = Response.ok(res[0] + " " + res[1], MediaType.TEXT_PLAIN).build();
        logger.log("GET_RESPONSE " + s.read() + " " + s.getStartTime());
        return response;
    }

}
