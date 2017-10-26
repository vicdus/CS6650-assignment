package server;

import utilities.DBConnectionPoolWrapper;
import utilities.RFIDLiftDataDAO;


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

    @GET
    public Response get(@PathParam("skierID") int skierID, @PathParam("dayNum") int dayNum) throws SQLException {
        Connection conn = DBConnectionPoolWrapper.getConnection();
        Integer[] res = new RFIDLiftDataDAO(conn).vert(skierID, dayNum);
        DbUtils.closeQuietly(conn);
        return Response.ok(res[0] + " " + res[1], MediaType.TEXT_PLAIN).build();
    }

}
