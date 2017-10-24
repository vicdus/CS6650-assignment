package server;

import Utilities.DBConnectionPoolWrapper;
import Utilities.RFIDLiftDataDAO;
import lombok.SneakyThrows;

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
    public Response get(@PathParam("skierID") int skierID, @PathParam("dayNum") int dayNum) throws SQLException {
        Integer[] res = new RFIDLiftDataDAO(DBConnectionPoolWrapper.getConnection()).vert(skierID, dayNum);
        return Response.ok(res[0] + " " + res[1], MediaType.TEXT_PLAIN).build();
    }

}
