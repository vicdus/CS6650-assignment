package server;

import Utilities.DBConnectionPoolWrapper;
import Utilities.RFIDLiftDataDAO;
import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("myvert/skierID/{skierID}/dayNum/{dayNum}")
@Produces(MediaType.TEXT_PLAIN)
public class MyVertService {

    @GET
    public Response get(@PathParam("skierID") int skierID, @PathParam("dayNum") int dayNum) {
        Connection conn = null;
        try {
            conn = DBConnectionPoolWrapper.getConnection();
            RFIDLiftDataDAO db = new RFIDLiftDataDAO(conn);
            return Response.ok(db.vert(skierID, dayNum).toString(), MediaType.TEXT_PLAIN).build();
        } catch (SQLException e) {
            return Response.serverError().build();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

}
